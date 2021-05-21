package util;

import static app.Main.settings;

import java.awt.Color;
import java.util.Arrays;

import types.GLSLType;

public final class TypeUtil {

	private static final String[] glslTypes = { "void", "bool", "int", "uint", "float", "bvec2", "bvec3", "bvec4",
			"ivec2", "ivec3", "ivec4", "uvec2", "uvec3", "uvec4", "vec2", "vec3", "vec4" };

	public static String getNameForType(GLSLType type) {
		if (type == null) {
			return null;
		}

		switch (type) {
		case VOID:
			return "void";
		case BOOL:
			return "bool";
		case INT:
			return "int";
		case UINT:
			return "uint";
		case FLOAT:
			return "float";
		case BOOL_VECTOR_2:
			return "bvec2";
		case BOOL_VECTOR_3:
			return "bvec3";
		case BOOL_VECTOR_4:
			return "bvec4";
		case INT_VECTOR_2:
			return "ivec2";
		case INT_VECTOR_3:
			return "ivec3";
		case INT_VECTOR_4:
			return "ivec4";
		case UINT_VECTOR_2:
			return "uvec2";
		case UINT_VECTOR_3:
			return "uvec3";
		case UINT_VECTOR_4:
			return "uvec4";
		case FLOAT_VECTOR_2:
			return "vec2";
		case FLOAT_VECTOR_3:
			return "vec3";
		case FLOAT_VECTOR_4:
			return "vec4";
		default:
			return null;
		}
	}

	public static GLSLType getTypeForName(String typeName) {
		if (typeName == null) {
			return null;
		}

		switch (typeName) {
		case "void":
			return GLSLType.VOID;
		case "bool":
			return GLSLType.BOOL;
		case "int":
			return GLSLType.INT;
		case "uint":
			return GLSLType.UINT;
		case "float":
			return GLSLType.FLOAT;
		case "bvec2":
			return GLSLType.BOOL_VECTOR_2;
		case "bvec3":
			return GLSLType.BOOL_VECTOR_3;
		case "bvec4":
			return GLSLType.BOOL_VECTOR_4;
		case "ivec2":
			return GLSLType.INT_VECTOR_2;
		case "ivec3":
			return GLSLType.INT_VECTOR_3;
		case "ivec4":
			return GLSLType.INT_VECTOR_4;
		case "uvec2":
			return GLSLType.UINT_VECTOR_2;
		case "uvec3":
			return GLSLType.UINT_VECTOR_3;
		case "uvec4":
			return GLSLType.UINT_VECTOR_4;
		case "vec2":
			return GLSLType.FLOAT_VECTOR_2;
		case "vec3":
			return GLSLType.FLOAT_VECTOR_3;
		case "vec4":
			return GLSLType.FLOAT_VECTOR_4;
		default:
			return GLSLType.NONE;
		}
	}

	public static Object getValueForType(GLSLType type, String value) {
		if (type == null || value == null || value.length() == 0) {
			return null;
		}

		try {
			switch (type) {
			case BOOL:
				return parseBool(value);
			case INT:
				return parseInt(value);
			case UINT:
				return parseUint(value);
			case FLOAT:
				return parseFloat(value);
			case BOOL_VECTOR_2:
				return parseBvec2(value);
			case BOOL_VECTOR_3:
				return parseBvec3(value);
			case BOOL_VECTOR_4:
				return parseBvec4(value);
			case INT_VECTOR_2:
				return parseIvec2(value);
			case INT_VECTOR_3:
				return parseIvec3(value);
			case INT_VECTOR_4:
				return parseIvec4(value);
			case UINT_VECTOR_2:
				return parseUvec2(value);
			case UINT_VECTOR_3:
				return parseUvec3(value);
			case UINT_VECTOR_4:
				return parseUvec4(value);
			case FLOAT_VECTOR_2:
				return parseVec2(value);
			case FLOAT_VECTOR_3:
				return parseVec3(value);
			case FLOAT_VECTOR_4:
				return parseVec4(value);
			case VOID:
			case NONE:
			default:
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	private static boolean parseBool(String value) {
		return value.equals("true") ? true : value.equals("false") ? false : null;
	}

	private static boolean[] parseBvec2(String value) {
		boolean[] result = new boolean[2];
		String typeName = "bvec2";
		if (value.startsWith(typeName + "(") && value.endsWith(")")) {
			String vectorValue = value.substring(typeName.length() + 1);
			vectorValue = vectorValue.substring(0, vectorValue.length() - 1);
			String[] parts = vectorValue.split(";");
			if (parts.length == result.length) {
				System.out.println(Arrays.toString(parts));
				for (int i = 0; i < parts.length; i++) {
					result[i] = parseBool(parts[i]);
				}
			}
		}

		return result;
	}

	private static boolean[] parseBvec3(String value) {
		boolean[] result = new boolean[3];
		String typeName = "bvec3";
		if (value.startsWith(typeName + "(") && value.endsWith(")")) {
			String vectorValue = value.substring(typeName.length() + 1);
			vectorValue = vectorValue.substring(0, vectorValue.length() - 1);
			String[] parts = vectorValue.split(";");
			if (parts.length == result.length) {
				System.out.println(Arrays.toString(parts));
				for (int i = 0; i < parts.length; i++) {
					result[i] = parseBool(parts[i]);
				}
			}
		}

		return result;
	}

	private static boolean[] parseBvec4(String value) {
		boolean[] result = new boolean[4];
		String typeName = "bvec4";
		if (value.startsWith(typeName + "(") && value.endsWith(")")) {
			String vectorValue = value.substring(typeName.length() + 1);
			vectorValue = vectorValue.substring(0, vectorValue.length() - 1);
			String[] parts = vectorValue.split(";");
			if (parts.length == result.length) {
				System.out.println(Arrays.toString(parts));
				for (int i = 0; i < parts.length; i++) {
					result[i] = parseBool(parts[i]);
				}
			}
		}

		return result;
	}

	private static int parseInt(String value) {
		return Integer.parseInt(value);
	}

	private static int[] parseIvec2(String value) {
		int[] result = new int[2];
		String typeName = "ivec2";
		if (value.startsWith(typeName + "(") && value.endsWith(")")) {
			String vectorValue = value.substring(typeName.length() + 1);
			vectorValue = vectorValue.substring(0, vectorValue.length() - 1);
			String[] parts = vectorValue.split(";");
			if (parts.length == result.length) {
				System.out.println(Arrays.toString(parts));
				for (int i = 0; i < parts.length; i++) {
					result[i] = parseInt(parts[i]);
				}
			}
		}

		return result;
	}

	private static int[] parseIvec3(String value) {
		int[] result = new int[3];
		String typeName = "ivec3";
		if (value.startsWith(typeName + "(") && value.endsWith(")")) {
			String vectorValue = value.substring(typeName.length() + 1);
			vectorValue = vectorValue.substring(0, vectorValue.length() - 1);
			String[] parts = vectorValue.split(";");
			if (parts.length == result.length) {
				System.out.println(Arrays.toString(parts));
				for (int i = 0; i < parts.length; i++) {
					result[i] = parseInt(parts[i]);
				}
			}
		}

		return result;
	}

	private static int[] parseIvec4(String value) {
		int[] result = new int[4];
		String typeName = "ivec4";
		if (value.startsWith(typeName + "(") && value.endsWith(")")) {
			String vectorValue = value.substring(typeName.length() + 1);
			vectorValue = vectorValue.substring(0, vectorValue.length() - 1);
			String[] parts = vectorValue.split(";");
			if (parts.length == result.length) {
				System.out.println(Arrays.toString(parts));
				for (int i = 0; i < parts.length; i++) {
					result[i] = parseInt(parts[i]);
				}
			}
		}

		return result;
	}

	private static int parseUint(String value) {
		return Integer.parseUnsignedInt(value);
	}

	private static int[] parseUvec2(String value) {
		int[] result = new int[2];
		String typeName = "uvec2";
		if (value.startsWith(typeName + "(") && value.endsWith(")")) {
			String vectorValue = value.substring(typeName.length() + 1);
			vectorValue = vectorValue.substring(0, vectorValue.length() - 1);
			String[] parts = vectorValue.split(";");
			if (parts.length == result.length) {
				System.out.println(Arrays.toString(parts));
				for (int i = 0; i < parts.length; i++) {
					result[i] = parseUint(parts[i]);
				}
			}
		}

		return result;
	}

	private static int[] parseUvec3(String value) {
		int[] result = new int[3];
		String typeName = "uvec3";
		if (value.startsWith(typeName + "(") && value.endsWith(")")) {
			String vectorValue = value.substring(typeName.length() + 1);
			vectorValue = vectorValue.substring(0, vectorValue.length() - 1);
			String[] parts = vectorValue.split(";");
			if (parts.length == result.length) {
				System.out.println(Arrays.toString(parts));
				for (int i = 0; i < parts.length; i++) {
					result[i] = parseUint(parts[i]);
				}
			}
		}

		return result;
	}

	private static int[] parseUvec4(String value) {
		int[] result = new int[4];
		String typeName = "uvec4";
		if (value.startsWith(typeName + "(") && value.endsWith(")")) {
			String vectorValue = value.substring(typeName.length() + 1);
			vectorValue = vectorValue.substring(0, vectorValue.length() - 1);
			String[] parts = vectorValue.split(";");
			if (parts.length == result.length) {
				System.out.println(Arrays.toString(parts));
				for (int i = 0; i < parts.length; i++) {
					result[i] = parseUint(parts[i]);
				}
			}
		}

		return result;
	}

	private static float parseFloat(String value) {
		return (float) Double.parseDouble(value);
	}

	private static float[] parseVec2(String value) {
		float[] result = new float[2];
		String typeName = "vec2";
		if (value.startsWith(typeName + "(") && value.endsWith(")")) {
			String vectorValue = value.substring(typeName.length() + 1);
			vectorValue = vectorValue.substring(0, vectorValue.length() - 1);
			String[] parts = vectorValue.split(";");
			if (parts.length == result.length) {
				System.out.println(Arrays.toString(parts));
				for (int i = 0; i < parts.length; i++) {
					result[i] = parseUint(parts[i]);
				}
			}
		}

		return result;
	}

	private static float[] parseVec3(String value) {
		float[] result = new float[3];
		String typeName = "vec3";
		if (value.startsWith(typeName + "(") && value.endsWith(")")) {
			String vectorValue = value.substring(typeName.length() + 1);
			vectorValue = vectorValue.substring(0, vectorValue.length() - 1);
			String[] parts = vectorValue.split(";");
			if (parts.length == result.length) {
				System.out.println(Arrays.toString(parts));
				for (int i = 0; i < parts.length; i++) {
					result[i] = parseUint(parts[i]);
				}
			}
		}

		return result;
	}

	private static float[] parseVec4(String value) {
		float[] result = new float[4];
		String typeName = "vec4";
		if (value.startsWith(typeName + "(") && value.endsWith(")")) {
			String vectorValue = value.substring(typeName.length() + 1);
			vectorValue = vectorValue.substring(0, vectorValue.length() - 1);
			String[] parts = vectorValue.split(";");
			if (parts.length == result.length) {
				System.out.println(Arrays.toString(parts));
				for (int i = 0; i < parts.length; i++) {
					result[i] = parseUint(parts[i]);
				}
			}
		}

		return result;
	}

	public static Color getColorForType(GLSLType type) {
		if (type == null) {
			return settings.defaultPalette.portColor;
		}

		switch (type) {
		case BOOL:
			return settings.defaultPalette.boolTypeColor;
		case INT:
			return settings.defaultPalette.intTypeColor;
		case UINT:
			return settings.defaultPalette.uintTypeColor;
		case FLOAT:
			return settings.defaultPalette.floatTypeColor;
		case BOOL_VECTOR_2:
		case BOOL_VECTOR_3:
		case BOOL_VECTOR_4:
		case INT_VECTOR_2:
		case INT_VECTOR_3:
		case INT_VECTOR_4:
		case UINT_VECTOR_2:
		case UINT_VECTOR_3:
		case UINT_VECTOR_4:
		case FLOAT_VECTOR_2:
		case FLOAT_VECTOR_3:
		case FLOAT_VECTOR_4:
			return settings.defaultPalette.vectorTypeColor;
		default:
			return settings.defaultPalette.portColor;
		}
	}

	public static int getTypeSize(String typeName) {
		GLSLType glslType = getTypeForName(typeName);
		switch (glslType) {
		case BOOL:
			return 1;
		case BOOL_VECTOR_2:
			return 2;
		case BOOL_VECTOR_3:
			return 3;
		case BOOL_VECTOR_4:
			return 4;
		case DOUBLE:
			return 8;
		case FLOAT:
			return 4;
		case FLOAT_VECTOR_2:
			return 8;
		case FLOAT_VECTOR_3:
			return 12;
		case FLOAT_VECTOR_4:
			return 16;
		case INT:
			return 4;
		case INT_VECTOR_2:
			return 8;
		case INT_VECTOR_3:
			return 12;
		case INT_VECTOR_4:
			return 16;
		case UINT:
			return 4;
		case UINT_VECTOR_2:
			return 8;
		case UINT_VECTOR_3:
			return 12;
		case UINT_VECTOR_4:
			return 16;
		case VOID:
		case NONE:
		default:
			return 0;
		}
	}

	public static String[] getAllTypes() {
		return glslTypes;
	}

}