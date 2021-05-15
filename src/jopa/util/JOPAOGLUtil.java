package jopa.util;

import static jopa.io.JOPALoader.loadStandardShader;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL.setCapabilities;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGB8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glIsTexture;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex2i;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL15.glIsBuffer;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_CURRENT_PROGRAM;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_SHADER_TYPE;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetAttachedShaders;
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
import static org.lwjgl.opengl.GL43.GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS;
import static org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER;
import static org.lwjgl.opengl.GL43.glDispatchCompute;
import static org.lwjgl.opengl.GL43.glGetDebugMessageLog;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFWVidMode;

import jopa.graphics.JOPAImage;
import jopa.playground.JOPASimulationScript;
import jopa.types.JOPAGLSLType;
import jopa.types.JOPAProjectType;
import jopa.types.JOPAResource;

public final class JOPAOGLUtil {

	private static long createWindowForOpenGLContext() {
		return createWindow(22, 22, false, null);
	}

	public static String getVersion() {
		createWindowForOpenGLContext();
		String version = glGetString(GL_VERSION);
		deleteContext();

		return version;
	}

	public static void printDebug() {
		int count = 1;
		int[] sources = new int[count];
		int[] types = new int[count];
		int[] ids = new int[count];
		int[] severities = new int[count];
		int[] lengths = new int[count];
		ByteBuffer log = ByteBuffer.allocate(256);
		glGetDebugMessageLog(count, sources, types, ids, severities, lengths, log);
		glGetError();
		System.out.println("[OPENGL] Denug:" + new String(log.array()));
	}

	public static boolean validateShader(String shaderCode, JOPAProjectType projectType) {
		long window = createWindowForOpenGLContext();
		if (window == 0) {
			return false;
		}
		int shader;
		switch (projectType) {
		case FRAGMENT:
			shader = createShader(GL_FRAGMENT_SHADER, shaderCode);
			break;
		case COMPUTE:
			shader = createShader(GL_COMPUTE_SHADER, shaderCode);
			break;
		default:
			return false;
		}
		closeWindow(window);
		deleteContext();
		if (shader == 0) {
			return false;
		}

		return true;
	}

	public static int createShader(int shaderType, String shaderCode) {
		int shader = glCreateShader(shaderType);
		glShaderSource(shader, shaderCode);
		glCompileShader(shader);
		int status = glGetShaderi(shader, GL_COMPILE_STATUS);
		if (status == GL_FALSE) {
			System.err.println("Shader compilation failed");
			System.err.println("Shader type: " + ((shaderType == GL_FRAGMENT_SHADER) ? "fragment"
					: (shaderType == GL_COMPUTE_SHADER) ? "compute" : "other"));
			System.err.println("Shader code:\n");
			String[] lines = shaderCode.split("\n");
			for (int i = 0; i < lines.length; i++) {
				String lineString = "" + i;
				while (lineString.length() < 3) {
					lineString = "0" + lineString;
				}
				System.err.println(lineString + ">" + lines[i]);
			}
			System.err.println(glGetShaderInfoLog(shader));
			glDeleteShader(shader);

			return 0;
		}

		return shader;
	}

	public static boolean deleteShader(int shader) {
		if (shader == 0) {
			return false;
		}
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
				if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_TRUE) {
					glAttachShader(program, shader);
				} else {
					System.err.println(glGetShaderInfoLog(shader));
				}
			}
		}
		glLinkProgram(program);
		if (glGetProgrami(program, GL_LINK_STATUS) == GL_FALSE) {
			for (int shader : shaders) {
				glDetachShader(program, shader);
			}
			glDeleteProgram(program);

			return 0;
		}

		return program;
	}

	public static boolean deleteProgram(int program) {
		if (program == 0) {
			return false;
		}
		if (!glIsProgram(program)) {
			return false;
		}
		if (glGetInteger(GL_CURRENT_PROGRAM) == program) {
			return false;
		}
		glDeleteProgram(program);

		return true;
	}

	public static long createWindow(int width, int height, boolean isFullscreen, JOPASimulationScript context) {
		glfwInit();

		long window;
		String title = "JOPA simulation window";
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
			JOPAResource windowSize = new JOPAResource(JOPAGLSLType.INT_VECTOR_2, "windowSize", size);
			context.addResource(windowSize);
			float aspect = (float) windowWidht[0] / windowHeight[0];
			JOPAResource aspectResource = new JOPAResource(JOPAGLSLType.FLOAT, "aspect", aspect);
			context.addResource(aspectResource);
		}

		return window;
	}

	public static int[] getScreenSize() {
		createWindowForOpenGLContext();
		long monitor = glfwGetPrimaryMonitor();
		GLFWVidMode videoMode = glfwGetVideoMode(monitor);
		deleteContext();

		return new int[] { videoMode.width(), videoMode.height() };
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

	public static void closeWindow(long window) {
		glfwSetWindowShouldClose(window, true);
	}

	public static void tick(long window, JOPASimulationScript context) {
		passVariables(context);

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
	}

	public static boolean checkWindow(long window) {
		glfwPollEvents();

		return !glfwWindowShouldClose(window);
	}

	public static boolean compute(int x, int y, int z) {
		int currentProgram = glGetInteger(GL_CURRENT_PROGRAM);
		if (currentProgram == 0) {
			System.err.println("Current program is NULL");

			return false;
		}
		if (x == 0) {
			System.err.println("x is 0");

			return false;
		}
		if (y == 0) {
			System.err.println("y is 0");

			return false;
		}
		if (z == 0) {
			System.err.println("z is 0");

			return false;
		}
		int maxInvocations = glGetInteger(GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS);
		if (x > maxInvocations) {
			System.err.println("x > GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS");

			return false;
		}
		if (y > maxInvocations) {
			System.err.println("y > GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS");

			return false;
		}
		if (z > maxInvocations) {
			System.err.println("z > GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS");

			return false;
		}

		int[] count = new int[1];
		int[] shaders = new int[64];
		glGetAttachedShaders(currentProgram, count, shaders);

		boolean hasComputeShader = false;
		for (int i = 0; i < count[0]; i++) {
			int shader = shaders[i];
			int shaderType = glGetShaderi(shader, GL_SHADER_TYPE);
			if (shaderType == GL_COMPUTE_SHADER) {
				hasComputeShader = true;
			}
		}
		if (!hasComputeShader) {
			System.err.println("Current program has no computer shader attached");

			return false;
		}
		glDispatchCompute(x, y, z);

		return true;
	}

	public static JOPAImage createTexture(int width, int height, int format) {
		if (width <= 0 || height <= 0 || format <= 0) {
			return null;
		}

		glEnable(GL_TEXTURE_2D);
		JOPAImage image = new JOPAImage(glGenTextures(), width, height, format);
		glBindTexture(GL_TEXTURE_2D, image.handle);
		glTexStorage2D(GL_TEXTURE_2D, 1, GL_RGBA32F, width, height);

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

	public static int createBuffer(ByteBuffer data) {
		int buffer = glGenBuffers();
		glBindBuffer(GL_SHADER_STORAGE_BUFFER, buffer);
		glBufferData(GL_SHADER_STORAGE_BUFFER, data, GL_DYNAMIC_DRAW);

		return buffer;
	}

	public static boolean deleteBuffer(int buffer) {
		if (!glIsBuffer(buffer)) {
			return false;
		}
		glDeleteBuffers(buffer);

		return true;
	}

	public static void deleteContext() {
		glfwTerminate();
		setCapabilities(null);
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

	public static void passVariables(JOPASimulationScript script) {
		if (script != null) {
			int program = glGetInteger(GL_CURRENT_PROGRAM);
			if (program != 0) {
				script.forEachResource(variable -> {
					String name = variable.name;
					if (name != null && name.length() > 0) {
						int location = glGetUniformLocation(program, name);
						if (location > -1) {
							if (passVariable(variable, location)) {
								System.out.println("[OPENGL] Variable " + name + " passed");
							}
						}
					}
				});
			}
		}
	}

	public static boolean passVariable(JOPAResource resource, int location) {
		if (resource != null && resource.type != null) {
			switch (resource.type) {
			case IMAGE:
			case BUFFER_HANDLE:
				return false;
			case GLSL_TYPE: {
				if (resource.glslType != null) {
					JOPAGLSLType type = resource.glslType;
					switch (type) {
					case BOOL: {
						int value = safeCast(resource.getAsObject(), boolean.class) ? 1 : 0;
						glUniform1i(location, value);
						break;
					}
					case INT: {
						int value = safeCast(resource.getAsObject(), int.class);
						glUniform1i(location, value);
						break;
					}
					case UINT: {
						int value = safeCast(resource.getAsObject(), int.class);
						glUniform1ui(location, value);
						break;
					}
					case FLOAT: {
						float value = safeCast(resource.getAsObject(), float.class);
						glUniform1f(location, value);
						break;
					}
					case DOUBLE: {
						double value = safeCast(resource.getAsObject(), double.class);
						glUniform1d(location, value);
						break;
					}
					case BOOL_VECTOR_2: {
						int[] value = safeCast(resource.getAsObject(), int[].class);
						glUniform2iv(location, value);
						break;
					}
					case BOOL_VECTOR_3: {
						int[] value = safeCast(resource.getAsObject(), int[].class);
						glUniform3iv(location, value);
						break;
					}
					case BOOL_VECTOR_4: {
						int[] value = safeCast(resource.getAsObject(), int[].class);
						glUniform4iv(location, value);
						break;
					}
					case INT_VECTOR_2: {
						int[] value = safeCast(resource.getAsObject(), int[].class);
						glUniform2iv(location, value);
						break;
					}
					case INT_VECTOR_3: {
						int[] value = safeCast(resource.getAsObject(), int[].class);
						glUniform3iv(location, value);
						break;
					}
					case INT_VECTOR_4: {
						int[] value = safeCast(resource.getAsObject(), int[].class);
						glUniform4iv(location, value);
						break;
					}
					case UINT_VECTOR_2: {
						int[] value = safeCast(resource.getAsObject(), int[].class);
						glUniform2iv(location, value);
						break;
					}
					case UINT_VECTOR_3: {
						int[] value = safeCast(resource.getAsObject(), int[].class);
						glUniform3iv(location, value);
						break;
					}
					case UINT_VECTOR_4: {
						int[] value = safeCast(resource.getAsObject(), int[].class);
						glUniform4iv(location, value);
						break;
					}
					case FLOAT_VECTOR_2: {
						float[] value = safeCast(resource.getAsObject(), float[].class);
						glUniform2fv(location, value);
						break;
					}
					case FLOAT_VECTOR_3: {
						float[] value = safeCast(resource.getAsObject(), float[].class);
						glUniform3fv(location, value);
						break;
					}
					case FLOAT_VECTOR_4: {
						float[] value = safeCast(resource.getAsObject(), float[].class);
						glUniform4fv(location, value);
						break;
					}
					// TODO remaining types
					case NONE: {
						return false;
					}
					default:
						return false;
					}
				}
			}
			default:
				return false;
			}
		}

		return true;
	}

}