package types;

import java.io.Serializable;

import graphics.Image;

public class Resource implements Serializable {

	private static final long serialVersionUID = 2431228582741056741L;

	private Object value;

	public ResourceType type;
	public GLSLType glslType;
	public String name;

	public Resource(ResourceType type, String name, Object value) {
		this.type = type;
		this.glslType = GLSLType.NONE;
		this.name = name;
		this.value = value;
	}

	public Resource(GLSLType type, String name, Object value) {
		this.type = ResourceType.GLSL_TYPE;
		this.glslType = type;
		this.name = name;
		this.value = value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public long getAsWindow() {
		if (type == ResourceType.WINDOW_HANDLE) {
			try {
				return (long) value;
			} catch (Exception e) {
				return 0;
			}
		}

		return 0;
	}

	public Image getAsImage() {
		if (type == ResourceType.IMAGE) {
			try {
				return (Image) value;
			} catch (Exception e) {
				return null;
			}
		}

		return null;
	}

	public Buffer getAsBuffer() {
		if (type == ResourceType.BUFFER) {
			try {
				return (Buffer) value;
			} catch (Exception e) {
				return null;
			}
		}

		return null;
	}

	public int getAsShader() {
		if (type == ResourceType.SHADER) {
			try {
				return (int) value;
			} catch (Exception e) {
				return 0;
			}
		}

		return 0;
	}

	public int getAsProgram() {
		if (type == ResourceType.PROGRAM) {
			try {
				return (int) value;
			} catch (Exception e) {
				return 0;
			}
		}

		return 0;
	}

	public int getAsLabel() {
		if (type == ResourceType.LABEL) {
			try {
				return (int) value;
			} catch (Exception e) {
				return -1;
			}
		}

		return -1;
	}

	public GLSLType getAsGLSLType() {
		if (type == ResourceType.GLSL_TYPE) {
			try {
				return (GLSLType) value;
			} catch (Exception e) {
				return null;
			}
		}

		return null;
	}

	public Object getAsObject() {
		return value;
	}

}