/*******************************************************************************
 * The MIT License
 * 
 * Copyright (C) 2020, CloudBees, Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/

package io.jenkins.plugins.bitbucketpushandpullrequest.filter.pullrequest.cloud;

import java.io.File;
import java.io.IOException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import hudson.EnvVars;
import hudson.Extension;
import io.jenkins.plugins.bitbucketpushandpullrequest.action.BitBucketPPRAction;
import io.jenkins.plugins.bitbucketpushandpullrequest.cause.BitBucketPPRTriggerCause;
import io.jenkins.plugins.bitbucketpushandpullrequest.cause.pullrequest.cloud.BitBucketPPRPullRequestCommentUpdatedCause;
import io.jenkins.plugins.bitbucketpushandpullrequest.util.BitBucketPPRUtils;

public class BitBucketPPRPullRequestCommentUpdatedActionFilter 
    extends BitBucketPPRPullRequestActionFilter {

  public String allowedBranches;
  public String commentFilter;
  public boolean isToApprove;

  @DataBoundConstructor
  public BitBucketPPRPullRequestCommentUpdatedActionFilter() {}

  @DataBoundSetter
  public void setAllowedBranches(String allowedBranches) {
    if (allowedBranches == null) {
      this.allowedBranches = "";
    } else {
      this.allowedBranches = allowedBranches;
    }
  }

  @DataBoundSetter
  public void setCommentFilter(String commentFilter) {
    if (commentFilter == null) {
      this.commentFilter = "";
    } else {
      this.commentFilter = commentFilter;
    }
  }

  @DataBoundSetter
  public void setIsToApprove(boolean isToApprove) {
    this.isToApprove = isToApprove;
  }

  @Override
  public boolean shouldTriggerBuild(BitBucketPPRAction bitbucketAction) {
    return matches(allowedBranches, bitbucketAction.getTargetBranch(), null)
        && hasInComment(bitbucketAction.getComment(), null);
  }

  @Override
  public BitBucketPPRTriggerCause getCause(File pollingLog, BitBucketPPRAction pullRequestAction)
      throws IOException {
    return new BitBucketPPRPullRequestCommentUpdatedCause(pollingLog, pullRequestAction);
  }

  @Override
  public boolean shouldSendApprove() {
    return isToApprove;
  }

  public boolean hasInComment(String comment, EnvVars vars) {
    return BitBucketPPRUtils.matchWithRegex(comment, commentFilter, vars);
  }

  @Extension
  public static class ActionFilterDescriptorImpl extends BitBucketPPRPullRequestActionDescriptor {

    @Override
    public String getDisplayName() {
      return "Comment Updated";
    }
  }

  @Override
  public String toString() {
    return "BitBucketPPRPullRequestCommentUpdatedActionFilter [getDescriptor()=" + getDescriptor()
        + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
        + super.toString() + "]";
  }
}