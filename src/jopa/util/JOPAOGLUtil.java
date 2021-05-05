package jopa.util;

import static jopa.io.JOPALoader.loadStandardShader;
import static org.lwjgl.opengl.GL43.*;

import java.util.Collection;

import jopa.types.JOPAGLSLType;
import jopa.types.JOPAResource;

public final class JOPAOGLUtil {

	@SuppressWarnings("unchecked")
	public static <T> T safeCast(Object object, Class<T> type) {
		if (object == null) {
			return (T) null;
		} else {
			try {
				return (T) object;
			} catch (Exception e) {
				return (T) null;
			}
		}
	}

	public static void passUniforms(Collection<JOPAResource> resources) {
		if (resources != null && resources.size() > 0) {
			int program = glGetInteger(GL_CURRENT_PROGRAM);
			if (program != 0) {
				for (JOPAResource resource : resources) {
					String name = resource.name;
					if (name != null && name.length() > 0) {
						int location = glGetUniformLocation(program, name);
						if (location > -1) {
							passUniformValue(resource, location);
						}
					}
				}
			}
		}
	}

	public static void passUniformValue(JOPAResource resource, int location) {
		if (resource != null && resource.type != null) {
			switch (resource.type) {
			case BUFFER_HANDLE:
			case TEXTURE_HANDLE: {
				int value = safeCast(resource.value, int.class);
				glUniform1i(location, value);
			}
			case GLSL_TYPE:
				if (resource.glslType != null) {
					JOPAGLSLType type = resource.glslType;
					switch (type) {
					case JOPA_BOOL: {
						int value = safeCast(resource.value, boolean.class) ? 1 : 0;
						glUniform1i(location, value);
						break;
					}
					case JOPA_INT: {
						int value = safeCast(resource.value, int.class);
						glUniform1i(location, value);
						break;
					}
					case JOPA_UINT: {
						int value = safeCast(resource.value, int.class);
						glUniform1ui(location, value);
						break;
					}
					case JOPA_FLOAT: {
						float value = safeCast(resource.value, float.class);
						glUniform1f(location, value);
						break;
					}
					case JOPA_DOUBLE: {
						double value = safeCast(resource.value, double.class);
						glUniform1d(location, value);
						break;
					}
					case JOPA_BOOL_VECTOR_2: {
						int[] value = safeCast(resource.value, int[].class);
						glUniform2iv(location, value);
						break;
					}
					case JOPA_BOOL_VECTOR_3: {
						int[] value = safeCast(resource.value, int[].class);
						glUniform3iv(location, value);
						break;
					}
					case JOPA_BOOL_VECTOR_4: {
						int[] value = safeCast(resource.value, int[].class);
						glUniform4iv(location, value);
						break;
					}
					case JOPA_INT_VECTOR_2: {
						int[] value = safeCast(resource.value, int[].class);
						glUniform2iv(location, value);
						break;
					}
					case JOPA_INT_VECTOR_3: {
						int[] value = safeCast(resource.value, int[].class);
						glUniform3iv(location, value);
						break;
					}
					case JOPA_INT_VECTOR_4: {
						int[] value = safeCast(resource.value, int[].class);
						glUniform4iv(location, value);
						break;
					}
					case JOPA_UINT_VECTOR_2: {
						int[] value = safeCast(resource.value, int[].class);
						glUniform2iv(location, value);
						break;
					}
					case JOPA_UINT_VECTOR_3: {
						int[] value = safeCast(resource.value, int[].class);
						glUniform3iv(location, value);
						break;
					}
					case JOPA_UINT_VECTOR_4: {
						int[] value = safeCast(resource.value, int[].class);
						glUniform4iv(location, value);
						break;
					}
					case JOPA_FLOAT_VECTOR_2: {
						float[] value = safeCast(resource.value, float[].class);
						glUniform2fv(location, value);
						break;
					}
					case JOPA_FLOAT_VECTOR_3: {
						float[] value = safeCast(resource.value, float[].class);
						glUniform3fv(location, value);
						break;
					}
					case JOPA_FLOAT_VECTOR_4: {
						float[] value = safeCast(resource.value, float[].class);
						glUniform4fv(location, value);
						break;
					}
					// TODO remaining
					case JOPA_NONE: {
						return;
					}
					default:
						return;
					}
				}
			default:
				return;
			}
		}
	}

	public static int loadShader(int shaderType, String fileName) {
		int shader = glCreateShader(shaderType);
		glShaderSource(shader, loadStandardShader(fileName));
		glCompileShader(shader);
		if (glGetShaderi(shaderType, GL_COMPILE_STATUS) == 1) {
			glDeleteShader(shader);

			return 0;
		}

		return shader;
	}

	public static int loadFragmentShader(String fileName) {
		return loadShader(GL_FRAGMENT_SHADER, fileName);
	}

	public static int loadComputeShader(String fileName) {
		return loadShader(GL_COMPUTE_SHADER, fileName);
	}

	public static int createProgram(int... shaders) {
		int program = glCreateProgram();
		for (int shader : shaders) {
			if (shader > 0) {
				if (glGetShaderi(shader, GL_COMPILE_STATUS) == 1) {
					glAttachShader(program, shader);
				}
			}
		}
		glLinkProgram(program);
		if (glGetProgrami(program, GL_LINK_STATUS) == 0) {
			for (int shader : shaders) {
				glDetachShader(program, shader);
			}
			glDeleteProgram(program);

			return 0;
		}

		return program;
	}

}