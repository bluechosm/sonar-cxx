/*
 * Sonar C++ Plugin (Community)
 * Copyright (C) 2011 Waleri Enns and CONTACT Software GmbH
 * sonarqube@googlegroups.com
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.cxx.checks;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.sonar.cxx.CxxAstScanner;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifierRule;

public class UseCorrectTypeCheckTest {

  @Rule
  public CheckMessagesVerifierRule checkMessagesVerifier = new CheckMessagesVerifierRule();

  @Test
  public void detected() {
    SourceFile file = CxxAstScanner.scanSingleFile(new File("src/test/resources/checks/UseCorrectTypesCheck.cc"), new UseCorrectTypeCheck());
    checkMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(7).withMessage("Use the correct type instead of NULL (1 times).")
      .next().atLine(10).withMessage("Use the correct type instead of WORD (1 times).")
      .next().atLine(11).withMessage("Use the correct type instead of DWORD (1 times).")
      .next().atLine(12).withMessage("Use the correct type instead of BOOL (1 times).")
      .next().atLine(16).withMessage("Use the correct type instead of BYTE (1 times).")
      .next().atLine(17).withMessage("Use the correct type instead of FLOAT (1 times).");
  }

}
