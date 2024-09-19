package servicraft.model;


public class DockerMinecraft {
    private boolean isServerRunning;

    public DockerMinecraft() {
        this.isServerRunning = false;
    }

    public boolean isServerRunning() {
        return isServerRunning;
    }

    public void setServerRunning(boolean isServerRunning) {
        this.isServerRunning = isServerRunning;
    }
}
