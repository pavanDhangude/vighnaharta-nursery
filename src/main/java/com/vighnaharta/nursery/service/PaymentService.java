package com.vighnaharta.nursery.service;

import com.vighnaharta.nursery.dto.PaymentRequestDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.transaction.Transactional;
import jakarta.mail.internet.MimeMessage;

import java.io.ByteArrayOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;
import com.vighnaharta.nursery.dto.PaymentResponseDTO;
import com.vighnaharta.nursery.entity.Booking;
import com.vighnaharta.nursery.entity.Payment;
import com.vighnaharta.nursery.enums.PaymentMethod;
import com.vighnaharta.nursery.enums.PaymentStatus;
import com.vighnaharta.nursery.repository.BookingRepository;
import com.vighnaharta.nursery.repository.PaymentRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final JavaMailSender mailSender;

    public PaymentService(PaymentRepository paymentRepository,
                          BookingRepository bookingRepository,
                          JavaMailSender mailSender) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.mailSender = mailSender;
    }

    // ✅ COD Payment create + Confirmation Email
    @Transactional
    public PaymentResponseDTO createCODPayment(PaymentRequestDTO dto) {
        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found: " + dto.getBookingId()));

        paymentRepository.findByBookingId(booking.getId()).ifPresent(p -> {
            throw new RuntimeException("Payment already exists for this booking");
        });

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(PaymentMethod.COD);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setTransactionId(null);

        Payment savedPayment = paymentRepository.save(payment);

        // Link payment to booking
        booking.setPayment(savedPayment);
        bookingRepository.save(booking);

        // Send confirmation email
        sendEmail(
            booking.getCustomerEmail(),
            "Order Confirmation - Vighnaharta Nursery",
            "Dear " + booking.getCustomerName() + ", Your order has been placed with Cash on Delivery."
        );

        return map(savedPayment);
    }

    // ✅ Get payment by booking
    public PaymentResponseDTO getPaymentByBooking(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found for booking: " + bookingId));
        return map(payment);
    }

    // mapper
    private PaymentResponseDTO map(Payment payment) {
        PaymentResponseDTO res = new PaymentResponseDTO();
        res.setPaymentId(payment.getId());
        res.setBookingId(payment.getBooking().getId());
        res.setAmount(payment.getAmount());
        res.setPaymentMethod(payment.getPaymentMethod());
        res.setPaymentStatus(payment.getPaymentStatus());
        res.setTransactionId(payment.getTransactionId());
        return res;
    }

    // email helper
    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom("pavandhangude28@gmail.com");
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(text);
            mailSender.send(mail);
        } catch (Exception e) {
            System.err.println("Email failed: " + e.getMessage());
        }
    }
    
    
    
 // ================== NEW METHODS START ==================

    @Transactional
    public Payment createOnlinePaymentAndLink(Booking booking, String razorpayPaymentId, String razorpayOrderId, double amount) {
        // ✅ Duplicate guard (same booking pe multiple payments block)
        paymentRepository.findByBookingId(booking.getId()).ifPresent(p -> {
            throw new RuntimeException("Payment already exists for this booking");
        });

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(amount);
        payment.setPaymentMethod(PaymentMethod.ONLINE);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(razorpayPaymentId); // payment_id
        payment.setOrderId(razorpayOrderId);         // order_id

        Payment saved = paymentRepository.save(payment);

        // ✅ Booking update: auto confirm + link payment
        booking.setPayment(saved);
        booking.setStatus("CONFIRMED");

        // ✅ Stock reduce (plant quantity se booking qty minus)
        if (booking.getPlant() != null && booking.getPlant().getQuantity() != null) {
            int newQty = Math.max(0, booking.getPlant().getQuantity() - booking.getQuantity());
            booking.getPlant().setQuantity(newQty);
        }
        bookingRepository.save(booking);

        return saved;
    }

    public void handlePostPaymentSuccess(Payment payment) {
        // 1) PDF invoice generate
        byte[] pdf = generateInvoicePdf(payment);

        // 2) Confirmation email with PDF
        sendMailWithAttachment(
            payment.getBooking().getCustomerEmail(),
            "Payment Success - Vighnaharta Nursery",
            "Dear " + payment.getBooking().getCustomerName() + ",\n\n" +
            "Your online payment is successful and your booking is confirmed.\n" +
            "Please find the attached invoice.\n\nRegards,\nVighnaharta Nursery",
            pdf,
            "invoice_" + payment.getId() + ".pdf"
        );
    }

    // --- Helpers ---

    private byte[] generateInvoicePdf(Payment payment) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document();
            PdfWriter.getInstance(doc, out);
            doc.open();
            Font title = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);

            doc.add(new Paragraph("Vighnaharta Nursery - Invoice", title));
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Invoice ID: INV-" + payment.getId()));
            doc.add(new Paragraph("Booking ID: " + payment.getBooking().getId()));
            doc.add(new Paragraph("Customer: " + payment.getBooking().getCustomerName()));
            doc.add(new Paragraph("Email: " + payment.getBooking().getCustomerEmail()));
            String plantName = (payment.getBooking().getPlant() != null) ? payment.getBooking().getPlant().getName() : "-";
            doc.add(new Paragraph("Plant: " + plantName));
            doc.add(new Paragraph("Quantity: " + payment.getBooking().getQuantity()));
            doc.add(new Paragraph("Amount Paid: ₹" + payment.getAmount()));
            doc.add(new Paragraph("Payment Method: ONLINE"));
            doc.add(new Paragraph("Razorpay Payment ID: " + payment.getTransactionId()));
            doc.add(new Paragraph("Razorpay Order ID: " + payment.getOrderId()));
            doc.add(new Paragraph("Date: " + java.time.LocalDateTime.now()));
            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            System.err.println("PDF generation failed: " + e.getMessage());
            return new byte[0];
        }
    }

    private void sendMailWithAttachment(String to, String subject, String text, byte[] file, String filename) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("pavandhangude28@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            if (file != null && file.length > 0) {
                helper.addAttachment(filename, new ByteArrayResource(file));
            }
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Email send failed: " + e.getMessage());
        }
    }
    // ================== NEW METHODS END ================== 
    
    
    
}
