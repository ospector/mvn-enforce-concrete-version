package com.develeap.enforce;


import org.apache.maven.artifact.Artifact;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.enforcer.AbstractNonCacheableEnforcerRule;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

import java.util.ArrayList;
import java.util.List;

/**
 * This rule checks that all dependencies use concrete versions.
 *
 * @author <a href="mailto:omri.spector@develeap.com">Omri Spector</a>
 * @version $Id$
 */
@SuppressWarnings("WeakerAccess")
public class ConcreteVersionRule
    extends AbstractNonCacheableEnforcerRule
{

  /**
   * Allows this rule to execute only when this project is a release.
   */
  private boolean checkOnSnapshotBuilds = false;

  /**
   * Allows this rule to be limited to range checking (which is weird).
   */
  private boolean allowSnapshotVersions = false;

  /**
   * Allows this rule to fail when the parent is defined as non concrete.
   */
  private boolean alsoCheckParentVersion = true;


  // Override parent to allow optional ignore of this rule.
  public void execute( EnforcerRuleHelper helper )
      throws EnforcerRuleException
  {
    MavenProject project = getProject( helper );

    if ( !checkOnSnapshotBuilds && project.getArtifact().isSnapshot()) return;

    List<String> violations = new ArrayList<String>();

    if (alsoCheckParentVersion)
    {
      Artifact parentArtifact = project.getParentArtifact();
      if (parentArtifact!=null) {
        String version = parentArtifact.getVersion();
        checkConcreteVersion(version, String.format("parent %s", parentArtifact.getArtifactId()), violations);
      }
    }

    for (Object depObj : project.getDependencies()) {
      Dependency dep = (Dependency) depObj;
      String version = dep.getVersion().trim();
      checkConcreteVersion(version,String.format("dependency: %s",dep.getArtifactId()),violations);
    }

    if (violations.size()>0) fail(violations,helper.getLog());
  }

  private void fail(List<String> violations, Log log) throws EnforcerRuleException {
    if (violations.size()>1) {
      int i=0;
      log.error(String.format("%d violations found:",violations.size()));
      for (String violation : violations) {
        log.error(String.format("%d. %s",++i,violation));
      }
    } else {
      log.error(violations.get(0));
    }
    throw new EnforcerRuleException("POM contains references to non-concrete artifacts");
  }

  private void checkConcreteVersion(String version, String description, List<String> violations) {
    if (!allowSnapshotVersions && version.endsWith("-SNAPSHOT")) {
      violations.add(String.format("Snapshot not allowed for %s", description));
    }
    char firstChar = version.charAt(0);
    if (firstChar=='[' || firstChar == '(') {
      violations.add(String.format("Range not acceptable for %s", description));
    }
  }

  private MavenProject getProject( EnforcerRuleHelper helper )
      throws EnforcerRuleException
  {
    try
    {
      return (MavenProject) helper.evaluate( "${project}" );
    }
    catch ( ExpressionEvaluationException eee )
    {
      throw new EnforcerRuleException( "Unable to retrieve the MavenProject: ", eee );
    }
  }

  @SuppressWarnings("unused")
  public final boolean isCheckOnSnapshotBuilds()
  {
    return checkOnSnapshotBuilds;
  }

  @SuppressWarnings("unused")
  public final void setCheckOnSnapshotBuilds(boolean checkOnSnapshotBuilds)
  {
    this.checkOnSnapshotBuilds = checkOnSnapshotBuilds;
  }

  @SuppressWarnings("unused")
  public final boolean isAllowSnapshotVersions()
  {
    return allowSnapshotVersions;
  }

  @SuppressWarnings("unused")
  public final void setAllowSnapshotVersions(boolean allowSnapshotVersions)
  {
    this.allowSnapshotVersions = allowSnapshotVersions;
  }

  @SuppressWarnings("unused")
  public final boolean isAlsoCheckParentVersion()
  {
    return alsoCheckParentVersion;
  }

  @SuppressWarnings("unused")
  public final void setAlsoCheckParentVersion(boolean alsoCheckParentVersion)
  {
    this.alsoCheckParentVersion = alsoCheckParentVersion;
  }

}
