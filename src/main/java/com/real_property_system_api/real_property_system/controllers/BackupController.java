package com.real_property_system_api.real_property_system.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.real_property_system_api.real_property_system.bodies.BackupFileInfo;
import com.real_property_system_api.real_property_system.bodies.BackupResult;
import com.real_property_system_api.real_property_system.responses.Codes;
import com.real_property_system_api.real_property_system.responses.GenericMessage;


@RestController
@RequestMapping("/api/utils/backup")
public class BackupController 
{
    private static final String BACKUP_DIR = "backups";
    @PostMapping("/add")
    public ResponseEntity<BackupResult> createBackup(@RequestParam("filename") String fileName,
        @RequestParam(required = false, name = "rewrite") Boolean rewrite)
    {

        createBackupDir();
        fileName = fileName + ".sql";
        int code = createNewBackup(fileName);

        if (code != 0)
        {
            switch (code)
            {
                case -1:
                {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
                case 1:
                {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
        }

        GenericMessage genericMessage = new GenericMessage();
        genericMessage.code = Codes.NoError;
        genericMessage.message = "Успешное резервное копирование";

        

        BackupResult result = new BackupResult();
        
        result.setMeta(genericMessage);
        
        result.setFile_info(getInfo(fileName));
        return ResponseEntity.ok(result);
    }


    @GetMapping("/all")
    public List<BackupFileInfo> getBackups()
    {

        if (isBackupDirExists())
        {
            Path dirPath = getBackupDirFullPath();
            File dir = dirPath.toFile();

            File[] files = dir.listFiles();

            List<BackupFileInfo> fileInfos = new ArrayList<>();

            for (File file : files)
            {
                var info = getInfo(file);
                fileInfos.add(info);
            }

            return fileInfos;

        }

        return Collections.emptyList();
    }


    @PostMapping("/restore")
    public ResponseEntity<String> restoreDatabaseFromFile(@RequestParam("file") String fileName)
    {
        if (fileName != null && !fileName.isEmpty())
        {
            int code = restoreDatabase(fileName);

            if (code == 0)
            {
                return ResponseEntity.ok().build();
            }

            else if (code == -1)
            {
                return ResponseEntity.notFound().build();
            }

        }

        return ResponseEntity.internalServerError().build();
    }

    private int restoreDatabase(String fileName)
    {
        if (isFileAlreadyExists(fileName))
        {
            final Path fileFromRead = getFilePath(fileName);

            //final String file = fileFromRead.toAbsolutePath().toString();
            
            ProcessBuilder processBuilder = new ProcessBuilder();

            processBuilder.command("psql", "-U", "mans", "RealPropertyDatabase");
            
            processBuilder.redirectInput(Redirect.from(fileFromRead.toFile()));

            var env = processBuilder.environment();

            env.put("PGPASSWORD", "psql_123");


            
            try 
            {
                Process process = processBuilder.start();


                BufferedReader r = process.errorReader();

                while (r.readLine() != null)
                {
                    System.out.println(r.readLine());
                }

                long exitCode = process.waitFor();
                


                return 0;
            }

            catch (IOException ex)
            {

            }

            catch (InterruptedException ex)
            {

            }

            catch (Exception ex)
            {

            }

            return 1;
        }

        return -1;
    }

    private BackupFileInfo getInfo(File file)
    {
        BackupFileInfo backupFileInfo = new BackupFileInfo();

        try 
        {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            FileTime fileTime = attr.creationTime();

            LocalDateTime dt = LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());

            backupFileInfo.setCreated(dt);
        }
        catch (IOException ex)
        {

        }

        backupFileInfo.setSize(file.length());
        backupFileInfo.setName(file.getName());
        backupFileInfo.setServerPath(file.getAbsolutePath());

        return backupFileInfo;
    }

    private BackupFileInfo getInfo(String fileName)
    {
        Path path = getFilePath(fileName);

        File file = path.toAbsolutePath().toFile();

        BackupFileInfo fileInfo = new BackupFileInfo();

        fileInfo.setName(file.getName());
        fileInfo.setServerPath(file.getAbsolutePath());
        fileInfo.setSize(file.length());

        try 
        {
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
            FileTime fileTime = attr.creationTime();

            LocalDateTime dt = LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());

            fileInfo.setCreated(dt);
        }
        catch (IOException ex)
        {

        }

        return fileInfo;

    }

    private Path getFilePath(String fileName)
    {
        return Paths.get(BACKUP_DIR, fileName);
    }

    private Path getBackupDirFullPath()
    {
        return Paths.get(BACKUP_DIR).toAbsolutePath();
    }

    private int createNewBackup(String fileName)
    {
        if (isFileAlreadyExists(fileName))
        {
            return -1;
        }

        final Path whereToWrite = getFilePath(fileName);

        try 
        {
            final String outFile = whereToWrite.toAbsolutePath().toString();
            final String outEncoding = "utf-8"; 
            ProcessBuilder builder = new ProcessBuilder("pg_dump","-E", outEncoding, "--no-comments" , "-d", "RealPropertyDatabase", "-U", "mans", "-f", outFile);
            
            var env = builder.environment();
            
            env.put("PGPASSWORD", "psql_123");

            Process process = builder.start();

            int exitCode = process.waitFor();



        }

        catch (IOException ex)
        {
            return 1;
        }
        catch (InterruptedException ex)
        {
            return 1;
        }

        return 0;
    }

    private boolean isFileAlreadyExists(String fileName)
    {
        return new File(getFilePath(fileName).toString()).exists();
    }

    private boolean isBackupDirExists()
    {
        var path = Paths.get(BACKUP_DIR);
        return path.toAbsolutePath().toFile().exists();
    }

    private boolean createBackupDir()
    {   
        try 
        {
            
            if (!isBackupDirExists())
            {
                Path dirPath = Paths.get(BACKUP_DIR).toAbsolutePath();
                File dir = Files.createDirectory(dirPath).toFile();

                return dir.exists();
            }

            return false;
        }
        catch (IOException ex)
        {
            
        }

        return false;
    }

}
