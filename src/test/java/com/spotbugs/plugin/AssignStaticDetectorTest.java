package com.spotbugs.plugin;

import edu.umd.cs.findbugs.BugCollection;
import edu.umd.cs.findbugs.test.SpotBugsRule;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcher;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcherBuilder;
import org.junit.Rule;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static edu.umd.cs.findbugs.test.CountMatcher.containsExactly;
import static org.hamcrest.MatcherAssert.assertThat;

public class AssignStaticDetectorTest {
  @Rule public SpotBugsRule spotbugs = new SpotBugsRule();

  //    @Test
  public void testGoodCase() {
    Path path =
        Paths.get("target/test-classes", "com.spotbugs.plugin".replace('.', '/'), "GoodCase.class");
    BugCollection bugCollection = spotbugs.performAnalysis(path);

    BugInstanceMatcher bugTypeMatcher = new BugInstanceMatcherBuilder().bugType("MY_BUG").build();
    assertThat(bugCollection, containsExactly(0, bugTypeMatcher));
  }

  @Test
  public void testBadCase() {
    Path path =
        Paths.get("target/test-classes", "com.spotbugs.plugin".replace('.', '/'), "BadCase.class");
    BugCollection bugCollection = spotbugs.performAnalysis(path);

    BugInstanceMatcher bugTypeMatcher_assign =
        new BugInstanceMatcherBuilder().bugType("ASSIGN_MUTABLE_STATIC_FIELD").build();
    assertThat(bugCollection, containsExactly(4, bugTypeMatcher_assign));
  }
}
