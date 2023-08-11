package com.crowdin.test;

import com.crowdin.client.Client;
import com.crowdin.client.core.http.exceptions.HttpBadRequestException;
import com.crowdin.client.core.http.exceptions.HttpException;
import com.crowdin.client.core.model.Credentials;
import com.crowdin.client.core.model.ResponseObject;
import com.crowdin.client.sourcefiles.SourceFilesApi;
import com.crowdin.client.sourcefiles.model.AddFileRequest;
import com.crowdin.client.sourcefiles.model.FileInfo;
import com.crowdin.client.storage.StorageApi;
import com.crowdin.client.storage.model.Storage;
import com.crowdin.test.model.Arguments;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.InputStream;

@Slf4j
public class CrowdinManager {
    private final StorageApi storageApi;
    private final SourceFilesApi sourceFilesApi;
    private final long projectId;

    public CrowdinManager(Arguments arguments) {
        Client client = new Client(new Credentials(arguments.getToken(), null));
        storageApi = client.getStorageApi();
        sourceFilesApi = client.getSourceFilesApi();
        projectId = arguments.getProjectId();
    }

    public Storage addStorage(File file) {
        InputStream inputStream = FileUtil.getInputStream(file);
        try {
            ResponseObject<Storage> responseObject = storageApi.addStorage(file.getName(), inputStream);
            return responseObject.getData();
        } catch (HttpException httpException) {
            handleHttpException(httpException);
        }
        FileUtil.closeStream(inputStream);
        return null;
    }

    public FileInfo addFile(Storage storage) {
        AddFileRequest addFileRequest = new AddFileRequest();
        addFileRequest.setStorageId(storage.getId());
        addFileRequest.setName(storage.getFileName());

        try {
            ResponseObject<? extends FileInfo> responseObject = sourceFilesApi.addFile(projectId, addFileRequest);
            return responseObject.getData();
        } catch (HttpBadRequestException badRequestException) {
            handleBadRequest(badRequestException);
        }
        return null;
    }

    private void handleBadRequest(HttpBadRequestException badRequestException) {
        badRequestException.getErrors().stream()
                .flatMap(errorHolder -> errorHolder.getError().getErrors().stream())
                .filter(error -> "notUnique".equals(error.getCode()))
                .forEach(ign -> log.error("This file is already exists in the Crowdin project"));
    }

    private void handleHttpException(HttpException httpException) {
        if ("401".equals(httpException.getError().getCode())) {
            log.error("Cannot authorize");
        }
    }
}
