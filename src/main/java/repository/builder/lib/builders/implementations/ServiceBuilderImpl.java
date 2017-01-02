package repository.builder.lib.builders.implementations;

import repository.builder.lib.constants.RepositoryConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static repository.builder.lib.constants.Constants.*;
import static repository.builder.lib.constants.ServiceConstants.*;

public class ServiceBuilderImpl extends AbstractBuilder {

    private String serviceClassDirectoryName;

    public ServiceBuilderImpl() {
    }

    public void build() {
        this.createServiceInterfaceJavaFile(createdRepositories);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createServiceInterfaceJavaFile(Map<String, String> repositoriesMap) {
        for (Map.Entry<String, String> classEntry : repositoriesMap.entrySet()) {
            String className = classEntry.getKey();
            this.serviceClassDirectoryName = className.replace(className.charAt(0), Character.toLowerCase(className.charAt(0)));
            File directory = new File(SOURCE_PATH + "/" + MAIN_PATH + "/" + SERVICE_DIRECTORY_NAME + "/" + serviceClassDirectoryName);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String serviceFileName = className + SERVICE_NAME;
            File serviceFile = new File(directory.getAbsolutePath(), serviceFileName + ".java");
            if (!serviceFile.exists()) {

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(serviceFile, true))) {

                    StringBuilder builder = new StringBuilder(32);

                    builder.append(PACKAGE).append(MAIN_PATH.replaceAll("/", "\\.")).append(".").append(SERVICE_DIRECTORY_NAME).append(".").append(serviceClassDirectoryName).append(";");
                    builder.append(System.lineSeparator());
                    builder.append(System.lineSeparator());
                    builder.append(String.format(SERVICE_INTERFACE_NAME, serviceFileName));
                    builder.append(System.lineSeparator());
                    builder.append(CLOSE_BRACKET);
                    writer.write(builder.toString());
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.createServiceJavaFile(serviceFileName, directory.getAbsoluteFile().toString(),classEntry.getValue());
            }
        }
    }

    private void createServiceJavaFile(String serviceInterfaceFileName, String directoryAbsolutePath,String repoName) {

        String serviceFileName = serviceInterfaceFileName + SERVICE_NAME_IMPL;
        File serviceFile = new File(directoryAbsolutePath, serviceFileName + ".java");
        if (!serviceFile.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(serviceFile, true))) {

                StringBuilder builder = new StringBuilder(32);
                builder.append(PACKAGE).append(MAIN_PATH.replaceAll("/", "\\.")).append(".").append(SERVICE_DIRECTORY_NAME).append(".").append(serviceClassDirectoryName).append(";");
                builder.append(System.lineSeparator());
                builder.append(System.lineSeparator());
                builder.append("import ").append(MAIN_PATH.replaceAll("/", "\\.")).append(".")
                        .append(RepositoryConstants.REPOSITORY_DIRECTORY_NAME)
                        .append(".")
                        .append(repoName)
                        .append(";");
                builder.append(System.lineSeparator());
                builder.append(SERVICE_ANNOTATIONS_IMPORT);
                builder.append(System.lineSeparator());
                builder.append(System.lineSeparator());
                builder.append(SERVICE_ANNOTATION);
                builder.append(System.lineSeparator());
                builder.append(TRANSACTIONAL_ANNOTATION);
                builder.append(System.lineSeparator());
                builder.append(String.format(SERVICE_CLASS_NAME, serviceFileName,serviceInterfaceFileName));
                builder.append(System.lineSeparator());
                builder.append(System.lineSeparator());
                String repoVariableName = repoName.replace(repoName.charAt(0),Character.toLowerCase(repoName.charAt(0)));
                builder.append(String.format(SERVICE_REPOSITORY_FINAL,repoName,repoVariableName));
                builder.append(System.lineSeparator());
                builder.append(System.lineSeparator());
                builder.append(AUTOWIRED_ANNOTATION);
                builder.append(System.lineSeparator());
                builder.append(String.format(SERVICE_CONSTRUCTOR,serviceFileName,repoName,repoVariableName,"this." + repoVariableName,repoVariableName));
                builder.append(System.lineSeparator());
                builder.append(System.lineSeparator());
                builder.append(CLOSE_BRACKET);
                writer.write(builder.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
