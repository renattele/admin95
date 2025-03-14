package ru.renattele.admin95.repository;

import ru.renattele.admin95.model.FileEntity;

import java.util.List;

public interface FileRepository {
    List<FileEntity> getFiles();
    boolean saveFile(FileEntity file);
    boolean deleteFile(String id);
}