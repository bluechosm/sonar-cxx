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
package org.sonar.cxx.checks;

import java.util.List;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.cxx.api.CppPunctuator;
import org.sonar.cxx.parser.CxxGrammarImpl;
import org.sonar.cxx.tag.Tag;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;
import org.sonar.squidbridge.checks.SquidCheck;

@Rule(key = "TooManyLinesOfCodeInFunction",
      name = "Avoid too many code lines in a single function",
      priority = Priority.MAJOR,
      tags = {Tag.BRAIN_OVERLOAD})
@ActivatedByDefault
@SqaleConstantRemediation("1h")

public class TooManyLinesOfCodeInFunctionCheck extends SquidCheck<Grammar> { 
  private static final int DEFAULT_MAXIMUM = 200;

  @RuleProperty(
      key = "max",
      description = "Maximum code lines allowed",
      defaultValue = "" + DEFAULT_MAXIMUM)
  private int max = DEFAULT_MAXIMUM;

  @Override
  public void init() {
    subscribeTo(CxxGrammarImpl.functionBody);
  }

  @Override
  public void leaveNode(AstNode node) {
    int lineCount = getNumberOfLine(node);
    if (lineCount > max) {
      getContext().createLineViolation(this,
          "The number of code lines in this function is {0,number,integer} which is greater than {1,number,integer} authorized.",
          node, lineCount, max);
    }
  }

  public static int getNumberOfLine(AstNode node) {
    List<AstNode> allChilds = node.getDescendants(CxxGrammarImpl.statement, CppPunctuator.CURLBR_LEFT, CppPunctuator.CURLBR_RIGHT);
    int lines = 1;
    int firstLine = node.getTokenLine();
    if (allChilds != null && !allChilds.isEmpty()) {
      int previousLine = firstLine;
      for (AstNode child : allChilds) {
        if (child.getTokenLine()!= previousLine ) {
          lines++;
          previousLine = child.getTokenLine();
        }
      }
    }
    return lines;
  }

  public void setMax(int max) {
    this.max = max;
  }

}
