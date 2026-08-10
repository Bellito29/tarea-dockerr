package com.example.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.demo.controller.rest.dto.AdminRequest;
import com.example.demo.controller.rest.dto.AdminResponse;
import com.example.demo.domain.Admin;

@Mapper(componentModel = "spring")
public interface IAdminMapper {

    AdminResponse adminToAdminResponse(Admin admin);

    List<AdminResponse> adminsToAdminsResponse(List<Admin> admins);

    Admin adminRequestToAdmin(AdminRequest adminRequest);
}
