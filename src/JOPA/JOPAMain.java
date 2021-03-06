package JOPA;

public class JOPAMain {

	static Object workspaceSync;
	static JOPAWorkspace currentWorkspace;
	static JOPAUI ui;

	public static void main(String[] args) {
		if (!checkVersion()) {
			return;
		}

		workspaceSync = new Object();

		setupUI();
		createNewWorkspace();
	}

	private static boolean checkVersion() {

		return true;
	}

	private static void setupUI() {
		ui = new JOPAUI();
		ui.setupWindow();
		ui.setupMenu();
		ui.setupCanvas();
	}

	static void createNewWorkspace() {
		synchronized (workspaceSync) {
			currentWorkspace = new JOPAWorkspace(ui, "New workspace");
		}

		ui.repaint();
	}

	static void openWorkspace() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {

			}
		}
	}

	static void saveWorkspace() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {

			}
		}
	}

	static void destroyWorkspace() {
		synchronized (workspaceSync) {
			currentWorkspace = null;
		}

		ui.repaint();
	}

	static void quit() {
		synchronized (workspaceSync) {
			saveWorkspace();
			currentWorkspace = null;
			ui.close();
		}
	}

	static void createNewNode() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {

			}
		}
	}

	static void validateNodes() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {

			}
		}
	}

	static void createNewFunction() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {

			}
		}
	}

	static void validateFunction() {
		synchronized (workspaceSync) {
			if (currentWorkspace != null) {

			}
		}
	}

	static void about() {

	}

	static void manual() {

	}

}