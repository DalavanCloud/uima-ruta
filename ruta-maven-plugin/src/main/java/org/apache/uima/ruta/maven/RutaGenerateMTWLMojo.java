/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.uima.ruta.maven;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.uima.ruta.resource.MultiTreeWordList;
import org.sonatype.plexus.build.incremental.BuildContext;

/**
 * Generate descriptors from UIMA Ruta script files.
 * 
 */
@Mojo(name = "mtwl", defaultPhase = LifecyclePhase.PROCESS_RESOURCES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class RutaGenerateMTWLMojo extends AbstractMojo {
  @Component
  private MavenProject project;

  @Component
  private BuildContext buildContext;

  /**
   * The file where the generated multi tree word list will be written.
   */
  @Parameter(defaultValue = "${project.build.directory}/generated-sources/ruta/resources/generated.mtwl", required = true)
  private File outputFile;

  /**
   * The source files for the multi tree word list.
   */
  @Parameter(required = true)
  private FileSet inputFiles;

  /**
   * Source file encoding.
   */
  @Parameter(defaultValue = "${project.build.sourceEncoding}", required = true)
  private String encoding;

  /**
   * Compress resulting multi tree word list.
   */
  @Parameter(defaultValue = "true", required = true)
  private boolean compress;

  public void execute() throws MojoExecutionException, MojoFailureException {
    File parentFile = outputFile.getParentFile();
    if (!parentFile.exists()) {
      parentFile.mkdirs();
      buildContext.refresh(parentFile);
    }
    List<File> files = null;
    try {
      files = Utils.getFiles(inputFiles);
    } catch (IOException e) {
      getLog().warn("Error accessing input files.", e);
    }

    MultiTreeWordList trie = null;
    try {
      trie = new MultiTreeWordList(files);
    } catch (IOException e) {
      getLog().warn("Error creating MTWL file.", e);
    }

    if (trie != null) {
      try {
        trie.createMTWLFile(outputFile.getAbsolutePath(), compress, encoding);
      } catch (IOException e) {
        getLog().warn("Error writing MTWL file.", e);
      }
    }

  }
}