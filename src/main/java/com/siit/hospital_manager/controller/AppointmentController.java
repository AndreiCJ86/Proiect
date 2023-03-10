package com.siit.hospital_manager.controller;

import com.siit.hospital_manager.model.dto.AppointmentDto;
import com.siit.hospital_manager.model.dto.CreateAppointmentDto;
import com.siit.hospital_manager.model.dto.UpdateAppointmentDto;
import com.siit.hospital_manager.service.AppointmentService;
import com.siit.hospital_manager.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/appointment")

@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    @GetMapping("/findAllByPatient")
    public String findAllFutureAppointmentsByPatient(Model model, Principal principal) {
        List<AppointmentDto> appointments = appointmentService.findFutureByUserName(principal.getName());
        model.addAttribute("appointments", appointments);

        return "appointment/viewAll";
    }

    @GetMapping("/findAllPreviousAppointments")
    public String findAllPreviousAppointmentsByPatient(Model model, Principal principal) {
        List<AppointmentDto> appointments = appointmentService.findPreviousByUserName(principal.getName());
        model.addAttribute("appointments", appointments);

        return "appointment/viewAll";
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAppointmentById(@PathVariable Integer id, Principal principal){
         appointmentService.deleteAppointmentByIdAndPatient(id, principal.getName());
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("doctors", doctorService.findAll());
        model.addAttribute("appointment", CreateAppointmentDto.builder().build());
        return "appointment/create";
    }

        @PostMapping("/submitCreateAppointmentForm")
    public String submitCreateAppointmentForm(@Valid CreateAppointmentDto createAppointmentDto, Principal principal){
            appointmentService.createAppointment(createAppointmentDto, principal.getName());
        return "redirect:/appointment/findAllByPatient";

    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateAppointment(@PathVariable Integer id, @Valid @RequestBody UpdateAppointmentDto updateAppointmentDto, Principal principal){
        appointmentService.updateAppointment(id, updateAppointmentDto, principal.getName());
    }
}
