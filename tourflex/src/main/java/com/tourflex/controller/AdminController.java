package com.tourflex.controller;

import com.tourflex.service.BookingService;
import com.tourflex.service.PaymentService;
import com.tourflex.service.RefundRequestService;
import com.tourflex.service.TourPackageService;
import com.tourflex.service.UserService;
import com.tourflex.service.HotelService;
import com.tourflex.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private BookingService bookingService;
    @Autowired private PaymentService paymentService;
    @Autowired private RefundRequestService refundRequestService;
    @Autowired private TourPackageService tourPackageService;
    @Autowired private UserService userService;
    @Autowired private HotelService hotelService;
    @Autowired private ReviewService reviewService;

    @GetMapping("")
    public String adminRoot(HttpSession session) {
        if (session.getAttribute("admin") != null) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/user/login-page";
    }

    @GetMapping("/login")
    public String showAdminLoginPage() {
        return "redirect:/user/login-page";
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/user/login-page";
        }

        // Stats
        model.addAttribute("totalBookings", bookingService.getAllBookings().size());
        model.addAttribute("totalPayments", paymentService.getAllPayments().size());
        model.addAttribute("totalRefunds", refundRequestService.getAllRefundRequests().size());
        model.addAttribute("totalPackages", tourPackageService.getAllPackages().size());
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("totalHotels", hotelService.getAllHotels().size());
        model.addAttribute("totalReviews", reviewService.getAllReviews().size());

        double totalRevenue = paymentService.getAllPayments().stream()
                .mapToDouble(p -> p.getAmount()).sum();
        model.addAttribute("totalRevenue", totalRevenue);

        // All data for inline tabs
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("packages", tourPackageService.getAllPackages());
        model.addAttribute("hotels", hotelService.getAllHotels());
        model.addAttribute("bookings", bookingService.getAllBookings());
        model.addAttribute("payments", paymentService.getAllPayments());
        model.addAttribute("refunds", refundRequestService.getAllRefundRequests());

        return "admin-dashboard";
    }

    // Admin: View all users (kept for direct link access)
    @GetMapping("/users")
    public String showUsers(Model model, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/user/login-page";
        }
        model.addAttribute("users", userService.getAllUsers());
        return "admin-users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable int id, HttpSession session) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/user/login-page";
        }
        userService.deleteUser(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("admin");
        return "redirect:/user/login-page";
    }
}