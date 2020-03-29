package com.example.controllers;

import com.example.models.Review;
import com.example.models.Role;
import com.example.models.User;
import com.example.repo.ReviewRepository;
import com.example.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;


@Controller
public class MainController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Map<String, Object> model) {
        model.put("name", "World!");
        return "home";
    }

    @GetMapping("/about")
    public String about(Map<String, Object> model) {
        model.put("title", "Страница про нас");
        return "about";
    }

    @GetMapping("/reviews")
    public String reviews(Map<String, Object> model) {
        Iterable<Review> reviews = reviewRepository.findAll();
        model.put("title", "Отзывы");
        model.put("reviews", reviews);
        return "reviews";
    }

    @PostMapping("/reviews-add")
    public String reviews_add(@RequestParam String review_title,
                              @RequestParam String review_text,
                              Map<String, Object> model) {
        Review review = new Review(review_title, review_text);
        reviewRepository.save(review);
        return "redirect:/reviews";
    }

    @GetMapping("/reviews/{id}")
    public String review_info(@PathVariable(value = "id") long reviewId,
                              Map<String, Object> model) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        ArrayList<Review> result = new ArrayList<>();
        review.ifPresent(result::add);
        model.put("review", result);
        return "review-info";
    }

    @GetMapping("/reviews/{id}/update")
    public String reviewUpdate(@PathVariable(value = "id") long reviewId,
                               Map<String, Object> model) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        ArrayList<Review> result = new ArrayList<>();
        review.ifPresent(result::add);
        model.put("review", result);
        return "review-update";
    }

    @PostMapping("/reviews/{id}/update")
    public String reviewsUpdateForm(@PathVariable(value = "id") long reviewId,
                                    @RequestParam String title,
                                    @RequestParam String review_text,
                                    Map<String, Object> model) throws ClassNotFoundException {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ClassNotFoundException());

        review.setReview_title(title);
        review.setReview_text(review_text);
        reviewRepository.save(review);

        return "redirect:/reviews/" + reviewId;
    }

    @PostMapping("/reviews/{id}/delete")
    public String reviewsUpdateForm(@PathVariable(value = "id") long reviewId,
                                    Map<String, Object> model) throws ClassNotFoundException {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ClassNotFoundException());

        reviewRepository.delete(review);
        return "redirect:/reviews";
    }

    @GetMapping("/reg")
    public String reg() {
        return "/reg";
    }

    @PostMapping("/reg")
    public String AddUser(User user, Map<String, Object> model) {
        user.setEnabled(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect:/";
    }


    @GetMapping("/user")
    public String usercab() { return "/user-cabinet";}

    @PostMapping("/user")
    public String getUserCab(Map<String, Object> model) {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String email = user.getEmail();
        System.out.println(email);
        return "/user-cabinet";
    }
}