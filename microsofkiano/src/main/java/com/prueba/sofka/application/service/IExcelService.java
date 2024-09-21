package com.prueba.sofka.application.service;

import org.springframework.web.multipart.MultipartFile;

public interface IExcelService {
    void uploadSofkianos(MultipartFile file) throws Exception;
   
}
