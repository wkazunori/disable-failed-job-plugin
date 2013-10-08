package disableFailedJob.disableFailedJob;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Descriptor;
import hudson.model.Result; 
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;


public class DisableFailedJob extends Publisher {
	
	private final String whenDisable;
	
	@DataBoundConstructor
	public DisableFailedJob(String whenDisable){
		this.whenDisable = whenDisable;
	}
	
	public String getWhenDisable() {
		return whenDisable;
	}

	@Override
	public Descriptor<Publisher> getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }
	
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.BUILD;
	}
	
	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException, IOException {
		
		boolean disableWhenFairue = false;
		boolean disableWhenUnstable = false;
		
		if (whenDisable.equals(ParameterDefinision.JOB_DISABLE_WHEN_ONLY_FAIRURE)){
			disableWhenFairue = true;
		} else if(whenDisable.equals(ParameterDefinision.JOB_DISABLE_WHEN_FAIRURE_AND_UNSTABLE)) {
			disableWhenFairue = true;
			disableWhenUnstable = true;
		} else if (whenDisable.equals(ParameterDefinision.JOB_DISABLE_WHEN_ONLY_UNSTABLE)){
			disableWhenUnstable = true;
		} else {
			return false;
		}
		
		if((disableWhenFairue && (build.getResult() == Result.FAILURE))
				||( disableWhenUnstable && (build.getResult() == Result.UNSTABLE)) ){
			build.getProject().disable();
			listener.getLogger().println("'Disable Failed Job Plugin' Disabled Job");
		} 
		
		return true;
	}
	
	@Extension
	public static final class DescriptorImpl extends Descriptor<Publisher> {
		
		private String whenDisable;
		
        public DescriptorImpl() {
            super(DisableFailedJob.class);
        }
        
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        public String getDisplayName() {
            return "When Failed To Build, Disable Job";
        }

        public String whenDisable(){
        	return whenDisable;
        }
    }
}
