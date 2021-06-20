package com.cloud.cloudstorage.repository;

import com.cloud.cloudstorage.model.File;
import com.cloud.cloudstorage.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findAllByUser_Login(String login, Sort sort);

    void removeFileByFilename(String fileName);

    File findByFilenameAndUser(String filename, User user);
}
