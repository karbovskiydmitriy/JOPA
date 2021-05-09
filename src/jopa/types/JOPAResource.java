package jopa.types;

import java.io.Serializable;

public class JOPAResource implements Serializable {

	private static final long serialVersionUID = 2431228582741056741L;

	private Object value;

	public JOPAResourceType type;
	public JOPAGLSLType glslType;
	public String name;

	public JOPAResource(JOPAResourceType type, String name, Object value) {
		this.type = type;
		this.glslType = JOPAGLSLType.JOPA_NONE;
		this.name = name;
		this.value = value;
	}

	public JOPAResource(JOPAGLSLType type, String name, Object value) {
		this.type = JOPAResourceType.GLSL_TYPE;
		this.glslType = type;
		this.name = name;
		this.value = value;
	}

	public long getAsWindow() {
		if (type == JOPAResourceType.WINDOW_HANDLE) {
			try {
				return (long) value;
			} catch (Exception e) {
				return 0;
			}
		}

		return 0;
	}

	public int getAsTexture() {
		if (type == JOPAResourceType.TEXTURE_HANDLE) {
			try {
				return (int) value;
			} catch (Exception e) {
				return 0;
			}
		}

		return 0;
	}

	public int getAsBuffer() {
		if (type == JOPAResourceType.BUFFER_HANDLE) {
			try {
				return (int) value;
			} catch (Exception e) {
				return 0;
			}
		}

		return 0;
	}

	public int getAsShader() {
		if (type == JOPAResourceType.SHADER) {
			try {
				return (int) value;
			} catch (Exception e) {
				return 0;
			}
		}

		return 0;
	}

	public int getAsProgram() {
		if (type == JOPAResourceType.PROGRAM) {
			try {
				return (int) value;
			} catch (Exception e) {
				return 0;
			}
		}

		return 0;
	}

	public JOPAGLSLType getAsGLSLType() {
		if (type == JOPAResourceType.GLSL_TYPE) {
			try {
				return (JOPAGLSLType) value;
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