package com.ecommerce.myapp.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/dashboard")
public class DashBoardController {
    @GetMapping("bill-statistical")
    public ResponseEntity<?> statistical(){

        return ResponseEntity.ok("statistical");
    }
}
