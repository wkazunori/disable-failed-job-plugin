package disableFailedProject.disableFailedProject;

import java.io.IOException;

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


public class DisableFailedProject extends Publisher {
	
	public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();
	
	DisableFailedProject(){
	}
	
	@Override
	public Descriptor<Publisher> getDescriptor() {
        return DESCRIPTOR;
    }
	
	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.BUILD;
	}
	
	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws InterruptedException, IOException {
		if (build.getResult() == Result.FAILURE || build.getResult() == Result.UNSTABLE){
			build.getProject().disable();
		}
		listener.getLogger().println("Complete Disable Job");
		return true;
	}
	
	@Extension
	public static final class DescriptorImpl extends Descriptor<Publisher> {
        public DescriptorImpl() {
            super(DisableFailedProject.class);
        }
        
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        public String getDisplayName() {
            return "When Failed To Build, Disable Job";
        }

        public DisableFailedProject newInstance(StaplerRequest req) throws FormException {
            return new DisableFailedProject();
        }
    }
}
