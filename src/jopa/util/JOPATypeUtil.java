package jopa.util;

import static jopa.main.JOPAMain.settings;

import java.awt.Color;

import jopa.types.JOPAGLSLType;

public final class JOPATypeUtil {

	private static final String[] glslTypes = { "void", "bool", "int", "uint", "float", "bvec2", "bvec3", "bvec4",
			"ivec2", "ivec3", "ivec4", "uvec2", "uvec3", "uvec4", "vec2", "vec3", "vec4", "TODO" };

	public static String getNameForType(JOPAGLSLType type) {
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
			return "TODO";
		}
	}

	public static JOPAGLSLType getTypeForName(String typeName) {
		if (typeName == null) {
			return null;
		}

		switch (typeName) {
		case "void":
			return JOPAGLSLType.VOID;
		case "bool":
			return JOPAGLSLType.BOOL;
		case "int":
			return JOPAGLSLType.INT;
		case "uint":
			return JOPAGLSLType.UINT;
		case "float":
			return JOPAGLSLType.FLOAT;
		case "bvec2":
			return JOPAGLSLType.BOOL_VECTOR_2;
		case "bvec3":
			return JOPAGLSLType.BOOL_VECTOR_3;
		case "bvec4":
			return JOPAGLSLType.BOOL_VECTOR_4;
		case "ivec2":
			return JOPAGLSLType.INT_VECTOR_2;
		case "ivec3":
			return JOPAGLSLType.INT_VECTOR_3;
		case "ivec4":
			return JOPAGLSLType.INT_VECTOR_4;
		case "uvec2":
			return JOPAGLSLType.UINT_VECTOR_2;
		case "uvec3":
			return JOPAGLSLType.UINT_VECTOR_3;
		case "uvec4":
			return JOPAGLSLType.UINT_VECTOR_4;
		case "vec2":
			return JOPAGLSLType.FLOAT_VECTOR_2;
		case "vec3":
			return JOPAGLSLType.FLOAT_VECTOR_3;
		case "vec4":
			return JOPAGLSLType.FLOAT_VECTOR_4;
		default:
			return JOPAGLSLType.NONE;
		}
	}

	public static Object getValueForType(JOPAGLSLType type, String value) {
		if (type == null || value == null || value.length() == 0) {
			return null;
		}

		try {
			switch (type) {
			case BOOL: {
				return value.equals("true") ? true : value.equals("false") ? false : null;
			}
			case INT: {
				return Integer.parseInt(value);
			}
			case UINT: {
				return Integer.parseUnsignedInt(value);
			}
			case FLOAT: {
				return (float) Double.parseDouble(value);
			}
			// DECIDE handling commas
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
			case VOID:
			case NONE:
			default:
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public static Color getColorForType(JOPAGLSLType type) {
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
		JOPAGLSLType glslType = getTypeForName(typeName);
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
			// TODO other types
			return 0;
		}
	}

	public static String[] getAllTypes() {
		return glslTypes;
	}

}