/*
 * Sonar C++ Plugin (Community)
 * Copyright (C) 2010-2017 SonarOpenCommunity
 * http://github.com/SonarOpenCommunity/sonar-cxx
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.cxx;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import org.sonar.cxx.api.CxxMetric;
import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.api.SourceProject;
import org.sonar.squidbridge.indexer.QueryByType;

import com.sonar.sslr.api.Grammar;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class CxxAstScannerTest {

  @Test
  public void files() throws UnsupportedEncodingException, IOException {
    
    CxxFileTester tester = CxxFileTesterHelper.CreateCxxFileTester("src/test/resources/metrics/trivial.cc", ".");    
    CxxFileTesterHelper.AddFileToContext(tester, "src/test/resources/metrics/trivial.cc");
        
    AstScanner<Grammar> scanner = CxxAstScanner.create(CxxFileTesterHelper.mockCxxLanguage(), new CxxConfiguration(CxxFileTesterHelper.mockCxxLanguage()), tester.sensorContext);
    scanner.scanFiles(new ArrayList<>(Arrays.asList(
      new File("src/test/resources/metrics/trivial.cc"),
      new File("src/test/resources/metrics/classes.cc")))
    );
    SourceProject project = (SourceProject) scanner.getIndex().search(new QueryByType(SourceProject.class)).iterator().next();
    assertThat(project.getInt(CxxMetric.FILES)).isEqualTo(2);
  }

  @Test
  public void comments() throws UnsupportedEncodingException, IOException {    
    CxxFileTester tester = CxxFileTesterHelper.CreateCxxFileTester("src/test/resources/metrics/comments.cc", ".");    
    SourceFile file = CxxAstScanner.scanSingleFile(tester.cxxFile, tester.sensorContext, CxxFileTesterHelper.mockCxxLanguage());
    assertThat(file.getInt(CxxMetric.COMMENT_LINES)).isEqualTo(6);
    assertThat(file.getNoSonarTagLines()).contains(8).hasSize(1);
  }

  @Test
  public void lines() throws UnsupportedEncodingException, IOException {
    CxxFileTester tester = CxxFileTesterHelper.CreateCxxFileTester("src/test/resources/metrics/classes.cc", ".");
    SourceFile file = CxxAstScanner.scanSingleFile(tester.cxxFile, tester.sensorContext, CxxFileTesterHelper.mockCxxLanguage());
    assertThat(file.getInt(CxxMetric.LINES)).isEqualTo(7);
  }

  @Test
  public void lines_of_code() throws UnsupportedEncodingException, IOException {
    
    CxxFileTester tester = CxxFileTesterHelper.CreateCxxFileTester("src/test/resources/metrics/classes.cc", ".");
    SourceFile file = CxxAstScanner.scanSingleFile(tester.cxxFile, tester.sensorContext, CxxFileTesterHelper.mockCxxLanguage());
    assertThat(file.getInt(CxxMetric.LINES_OF_CODE)).isEqualTo(5);
  }

  @Test
  public void statements() throws UnsupportedEncodingException, IOException {
    CxxFileTester tester = CxxFileTesterHelper.CreateCxxFileTester("src/test/resources/metrics/statements.cc", ".");
    SourceFile file = CxxAstScanner.scanSingleFile(tester.cxxFile, tester.sensorContext, CxxFileTesterHelper.mockCxxLanguage());
    assertThat(file.getInt(CxxMetric.STATEMENTS)).isEqualTo(4);
  }

  @Test
  public void functions() throws UnsupportedEncodingException, IOException {
    CxxFileTester tester = CxxFileTesterHelper.CreateCxxFileTester("src/test/resources/metrics/functions.cc", ".");    
    SourceFile file = CxxAstScanner.scanSingleFile(tester.cxxFile, tester.sensorContext, CxxFileTesterHelper.mockCxxLanguage());
    assertThat(file.getInt(CxxMetric.FUNCTIONS)).isEqualTo(2);
  }

  @Test
  public void classes() throws UnsupportedEncodingException, IOException {
    CxxFileTester tester = CxxFileTesterHelper.CreateCxxFileTester("src/test/resources/metrics/classes.cc", ".");
    SourceFile file = CxxAstScanner.scanSingleFile(tester.cxxFile, tester.sensorContext, CxxFileTesterHelper.mockCxxLanguage());
    assertThat(file.getInt(CxxMetric.CLASSES)).isEqualTo(2);
  }

  @Test
  public void complexity() throws UnsupportedEncodingException, IOException {
    CxxFileTester tester = CxxFileTesterHelper.CreateCxxFileTester("src/test/resources/metrics/complexity.cc", ".");    
    SourceFile file = CxxAstScanner.scanSingleFile(tester.cxxFile, tester.sensorContext, CxxFileTesterHelper.mockCxxLanguage());
    assertThat(file.getInt(CxxMetric.COMPLEXITY)).isEqualTo(14);
  }

  @Test
  public void error_recovery_declaration() throws UnsupportedEncodingException, IOException {
    CxxFileTester tester = CxxFileTesterHelper.CreateCxxFileTester("src/test/resources/parser/bad/error_recovery_declaration.cc", ".");    
    SourceFile file = CxxAstScanner.scanSingleFile(tester.cxxFile, tester.sensorContext, CxxFileTesterHelper.mockCxxLanguage());
    assertThat(file.getInt(CxxMetric.FUNCTIONS)).isEqualTo(2);
  }
}
