package jopa.util;

import static jopa.io.JOPALoader.loadStandardShader;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL.setCapabilities;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGB8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glIsTexture;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex2i;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glIsBuffer;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_CURRENT_PROGRAM;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glIsProgram;
import static org.lwjgl.opengl.GL20.glIsShader;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2fv;
import static org.lwjgl.opengl.GL20.glUniform2iv;
import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUniform3iv;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.glUniform4iv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.GL_R16I;
import static org.lwjgl.opengl.GL30.GL_R16UI;
import static org.lwjgl.opengl.GL30.GL_R32F;
import static org.lwjgl.opengl.GL30.GL_R32I;
import static org.lwjgl.opengl.GL30.GL_R32UI;
import static org.lwjgl.opengl.GL30.GL_R8I;
import static org.lwjgl.opengl.GL30.GL_R8UI;
import static org.lwjgl.opengl.GL30.GL_RG16I;
import static org.lwjgl.opengl.GL30.GL_RG16UI;
import static org.lwjgl.opengl.GL30.GL_RG32F;
import static org.lwjgl.opengl.GL30.GL_RG32I;
import static org.lwjgl.opengl.GL30.GL_RG32UI;
import static org.lwjgl.opengl.GL30.GL_RG8I;
import static org.lwjgl.opengl.GL30.GL_RG8UI;
import static org.lwjgl.opengl.GL30.GL_RGB16I;
import static org.lwjgl.opengl.GL30.GL_RGB16UI;
import static org.lwjgl.opengl.GL30.GL_RGB32F;
import static org.lwjgl.opengl.GL30.GL_RGB32I;
import static org.lwjgl.opengl.GL30.GL_RGB32UI;
import static org.lwjgl.opengl.GL30.GL_RGB8I;
import static org.lwjgl.opengl.GL30.GL_RGB8UI;
import static org.lwjgl.opengl.GL30.GL_RGBA16I;
import static org.lwjgl.opengl.GL30.GL_RGBA16UI;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL30.GL_RGBA32I;
import static org.lwjgl.opengl.GL30.GL_RGBA32UI;
import static org.lwjgl.opengl.GL30.GL_RGBA8I;
import static org.lwjgl.opengl.GL30.GL_RGBA8UI;
import static org.lwjgl.opengl.GL30.glUniform1ui;
import static org.lwjgl.opengl.GL40.glUniform1d;
import static org.lwjgl.opengl.GL42.glTexStorage2D;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWVidMode;

import jopa.graphics.JOPAImage;
import jopa.playground.JOPASimulationScript;
import jopa.types.JOPAGLSLType;
import jopa.types.JOPAResource;

public final class JOPAOGLUtil {

	public static String getVersion() {
		long window = createWindow(22, 22, false, null);
		String version = glGetString(GL_VERSION);
		destroyWindow(window);

		return version;
	}

	public static int createShader(int shaderType, String code) {
		int shader = glCreateShader(shaderType);
		glShaderSource(shader, code);
		glCompileShader(shader);
		if (glGetShaderi(shaderType, GL_COMPILE_STATUS) == 1) {
			glDeleteShader(shader);

			return 0;
		}

		return shader;
	}
	
	public static boolean deleteShader(int shader) {
		if (!glIsShader(shader)) {
			return false;
		}
		glDeleteShader(shader);
		
		return true;
	}

	public static int loadFragmentShader(String fileName) {
		return createShader(GL_FRAGMENT_SHADER, loadStandardShader(fileName));
	}

	public static int loadComputeShader(String fileName) {
		return createShader(GL_COMPUTE_SHADER, loadStandardShader(fileName));
	}

	public static int createProgram(int... shaders) {
		int program = glCreateProgram();
		for (int shader : shaders) {
			if (shader > 0) {
				if (glGetShaderi(shader, GL_COMPILE_STATUS) == 1) {
					glAttachShader(program, shader);
				} else {
					System.err.println(glGetShaderInfoLog(shader));
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
	
	public static boolean deleteProgram(int program) {
		if (!glIsProgram(program)) {
			return false;
		}
		if (glGetInteger(GL_CURRENT_PROGRAM) == program) {
			glUseProgram(0); // DECIDE return false?..
		}
		glDeleteProgram(program);
		
		return true;
	}

	public static long createWindow(int width, int height, boolean isFullscreen, JOPASimulationScript context) {
		glfwInit();

		long window;
		String title = "Default fragment shader showcase";
		if (!isFullscreen) {
			glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
			window = glfwCreateWindow(width, height, title, NULL, NULL);
		} else {
			long monitor = glfwGetPrimaryMonitor();
			GLFWVidMode videoMode = glfwGetVideoMode(monitor);
			window = glfwCreateWindow(videoMode.width(), videoMode.height(), title, monitor, NULL);
		}
		int[] windowWidht = new int[1];
		int[] windowHeight = new int[1];
		glfwGetWindowSize(window, windowWidht, windowHeight);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);

		createCapabilities();

		glViewport(0, 0, windowWidht[0], windowHeight[0]);

		if (context != null) {
			int[] size = new int[] { windowWidht[0], windowHeight[0] };
			JOPAResource windowSize = new JOPAResource(JOPAGLSLType.JOPA_INT_VECTOR_2, "windowSize", size);
			context.addResource(windowSize);
		}

		return window;
	}

	public static int[] getWindowSize(long window) {
		if (window <= 0) {
			return null;
		}
		int[] windowWidht = new int[1];
		int[] windowHeight = new int[1];
		glfwGetWindowSize(window, windowWidht, windowHeight);
		int[] windowSize = new int[] { windowWidht[0], windowHeight[0] };

		return windowSize;
	}

	public static boolean tick(long window, JOPASimulationScript context) {
		if (!glfwWindowShouldClose(window)) {
			passUniforms(context);

			glClear(GL_COLOR_BUFFER_BIT);
			glBegin(GL_QUADS);
			glTexCoord2f(0, 1);
			glVertex2i(-1, -1);
			glTexCoord2f(0, 0);
			glVertex2i(-1, 1);
			glTexCoord2f(1, 0);
			glVertex2i(1, 1);
			glTexCoord2f(1, 1);
			glVertex2i(1, -1);
			glEnd();

			glfwSwapBuffers(window);
			glfwPollEvents();

			return true;
		}

		return false;
	}

	public static void destroyWindow(long window) {
		setCapabilities(null);
		glfwDestroyWindow(window);
		glfwTerminate();
	}

	public static JOPAImage createTexture(int width, int height, int format) {
		if (width <= 0 || height <= 0 || format <= 0) {
			return null;
		}

		glEnable(GL_TEXTURE_2D);
		JOPAImage image = new JOPAImage(width, height, glGenTextures());
		glBindTexture(GL_TEXTURE_2D, image.handle);
		glTexStorage2D(GL_TEXTURE_2D, 1, format, width, height);

		return image;
	}

	public static JOPAImage loadTexture(String fileName) {
		JOPAImage image = new JOPAImage(fileName);
		if (image.handle == 0) {
			return null;
		}

		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, image.handle);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, image.width, image.height, 0, GL_RGB, GL_UNSIGNED_BYTE, image.data);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		return image;
	}

	public static boolean deleteTexture(JOPAImage image) {
		if (!glIsTexture(image.handle)) {
			return false;
		}
		glDeleteTextures(image.handle);

		return true;
	}
	
	public static int createBuffer() {
		// TODO createBuffer
		
		return 42;
	}
	
	public static boolean deleteBuffer(int buffer) {
		if (!glIsBuffer(buffer)) {
			return false;
		}
		glDeleteBuffers(buffer);
		
		return true;
	}

	public static int getTextureFormat(int channels, Class<?> type, boolean signed) {
		if (type.equals(int.class)) {
			if (!signed) {
				switch (channels) {
				case 1:
					return GL_R32UI;
				case 2:
					return GL_RG32UI;
				case 3:
					return GL_RGB32UI;
				case 4:
					return GL_RGBA32UI;
				}
			} else {
				switch (channels) {
				case 1:
					return GL_R32I;
				case 2:
					return GL_RG32I;
				case 3:
					return GL_RGB32I;
				case 4:
					return GL_RGBA32I;
				}
			}
		} else if (type.equals(float.class)) {
			if (!signed) {
				switch (channels) {
				case 1:
					return GL_R32F;
				case 2:
					return GL_RG32F;
				case 3:
					return GL_RGB32F;
				case 4:
					return GL_RGBA32F;
				}
			}
		} else if (type.equals(short.class)) {
			if (!signed) {
				switch (channels) {
				case 1:
					return GL_R16UI;
				case 2:
					return GL_RG16UI;
				case 3:
					return GL_RGB16UI;
				case 4:
					return GL_RGBA16UI;
				}
			} else {
				switch (channels) {
				case 1:
					return GL_R16I;
				case 2:
					return GL_RG16I;
				case 3:
					return GL_RGB16I;
				case 4:
					return GL_RGBA16I;
				}
			}
		} else if (type.equals(byte.class)) {
			if (!signed) {
				switch (channels) {
				case 1:
					return GL_R8UI;
				case 2:
					return GL_RG8UI;
				case 3:
					return GL_RGB8UI;
				case 4:
					return GL_RGBA8UI;
				}
			} else {
				switch (channels) {
				case 1:
					return GL_R8I;
				case 2:
					return GL_RG8I;
				case 3:
					return GL_RGB8I;
				case 4:
					return GL_RGBA8I;
				}
			}
		}

		return 0;
	}

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

	public static void passUniforms(JOPASimulationScript script) {
		if (script != null) {
			int program = glGetInteger(GL_CURRENT_PROGRAM);
			if (program != 0) {
				script.forEachResource(resource -> {
					String name = resource.name;
					if (name != null && name.length() > 0) {
						int location = glGetUniformLocation(program, name);
						if (location > -1) {
							passUniformValue(resource, location);
						}
					}
				});
			}
		}
	}

	public static void passUniformValue(JOPAResource resource, int location) {
		if (resource != null && resource.type != null) {
			switch (resource.type) {
			case BUFFER_HANDLE:
				int buffer = safeCast(resource.getAsBuffer(), int.class);
				// FIXME passing buffer
				glUniform1i(location, buffer);
				break;
			case IMAGE: {
				int value = safeCast(resource.getAsImage(), int.class);
				glUniform1i(location, value);
				break;
			}
			case GLSL_TYPE:
				if (resource.glslType != null) {
					JOPAGLSLType type = resource.glslType;
					switch (type) {
					case JOPA_BOOL: {
						int value = safeCast(resource.getAsObject(), boolean.class) ? 1 : 0;
						glUniform1i(location, value);
						break;
					}
					case JOPA_INT: {
						int value = safeCast(resource.getAsObject(), int.class);
						glUniform1i(location, value);
						break;
					}
					case JOPA_UINT: {
						int value = safeCast(resource.getAsObject(), int.class);
						glUniform1ui(location, value);
						break;
					}
					case JOPA_FLOAT: {
						float value = safeCast(resource.getAsObject(), float.class);
						glUniform1f(location, value);
						break;
					}
					case JOPA_DOUBLE: {
						double value = safeCast(resource.getAsObject(), double.class);
						glUniform1d(location, value);
						break;
					}
					case JOPA_BOOL_VECTOR_2: {
						int[] value = safeCast(resource.getAsObject(), int[].class);
						glUniform2iv(location, value);
						break;
					}
					case JOPA_BOOL_VECTOR_3: {
						int[] value = safeCast(resource.getAsObject(), int[].class);
						glUniform3iv(location, value);
						break;
					}
					case JOPA_BOOL_VECTOR_4: {
						int[] value = safeCast(resource.getAsObject(), int[].class);
						glUniform4iv(location, value);
						break;
					}
					case JOPA_INT_VECTOR_2: {
						int[] value = safeCast(resource.getAsObject(), int[].class);
						glUniform2iv(location, value);
						break;
					}
					case JOPA_INT_VECTOR_3: {
						int[] value = safeCast(resource.getAsObject(), int[].class);
						glUniform3iv(location, value);
						break;
					}
					case JOPA_INT_VECTOR_4: {
						int[] value = safeCast(resource.getAsObject(), int[].class);
						glUniform4iv(location, value);
						break;
					}
					case JOPA_UINT_VECTOR_2: {
						int[] value = safeCast(resource.getAsObject(), int[].class);
						glUniform2iv(location, value);
						break;
					}
					case JOPA_UINT_VECTOR_3: {
						int[] value = safeCast(resource.getAsObject(), int[].class);
						glUniform3iv(location, value);
						break;
					}
					case JOPA_UINT_VECTOR_4: {
						int[] value = safeCast(resource.getAsObject(), int[].class);
						glUniform4iv(location, value);
						break;
					}
					case JOPA_FLOAT_VECTOR_2: {
						float[] value = safeCast(resource.getAsObject(), float[].class);
						glUniform2fv(location, value);
						break;
					}
					case JOPA_FLOAT_VECTOR_3: {
						float[] value = safeCast(resource.getAsObject(), float[].class);
						glUniform3fv(location, value);
						break;
					}
					case JOPA_FLOAT_VECTOR_4: {
						float[] value = safeCast(resource.getAsObject(), float[].class);
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

}