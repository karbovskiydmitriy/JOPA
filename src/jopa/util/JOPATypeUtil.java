package jopa.util;

import java.awt.Color;

import jopa.main.JOPAMain;
import jopa.types.JOPAGLSLType;

public final class JOPATypeUtil {

	private static final String[] glslTypes = { "void", "bool", "int", "uint", "float", "bvec2", "bvec3", "bvec4",
			"ivec2", "ivec3", "ivec4", "uvec2", "uvec3", "uvec4", "vec2", "vec3", "vec4", "TODO" };

	public static String getNameForType(JOPAGLSLType type) {
		if (type == null) {
			return null;
		}

		switch (type) {
		case JOPA_VOID:
			return "void";
		case JOPA_BOOL:
			return "bool";
		case JOPA_INT:
			return "int";
		case JOPA_UINT:
			return "uint";
		case JOPA_FLOAT:
			return "float";
		case JOPA_BOOL_VECTOR_2:
			return "bvec2";
		case JOPA_BOOL_VECTOR_3:
			return "bvec3";
		case JOPA_BOOL_VECTOR_4:
			return "bvec4";
		case JOPA_INT_VECTOR_2:
			return "ivec2";
		case JOPA_INT_VECTOR_3:
			return "ivec3";
		case JOPA_INT_VECTOR_4:
			return "ivec4";
		case JOPA_UINT_VECTOR_2:
			return "uvec2";
		case JOPA_UINT_VECTOR_3:
			return "uvec3";
		case JOPA_UINT_VECTOR_4:
			return "uvec4";
		case JOPA_FLOAT_VECTOR_2:
			return "vec2";
		case JOPA_FLOAT_VECTOR_3:
			return "vec3";
		case JOPA_FLOAT_VECTOR_4:
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
			return JOPAGLSLType.JOPA_VOID;
		case "bool":
			return JOPAGLSLType.JOPA_BOOL;
		case "int":
			return JOPAGLSLType.JOPA_INT;
		case "uint":
			return JOPAGLSLType.JOPA_UINT;
		case "float":
			return JOPAGLSLType.JOPA_FLOAT;
		case "bvec2":
			return JOPAGLSLType.JOPA_BOOL_VECTOR_2;
		case "bvec3":
			return JOPAGLSLType.JOPA_BOOL_VECTOR_3;
		case "bvec4":
			return JOPAGLSLType.JOPA_BOOL_VECTOR_4;
		case "ivec2":
			return JOPAGLSLType.JOPA_INT_VECTOR_2;
		case "ivec3":
			return JOPAGLSLType.JOPA_INT_VECTOR_3;
		case "ivec4":
			return JOPAGLSLType.JOPA_INT_VECTOR_4;
		case "uvec2":
			return JOPAGLSLType.JOPA_UINT_VECTOR_2;
		case "uvec3":
			return JOPAGLSLType.JOPA_UINT_VECTOR_3;
		case "uvec4":
			return JOPAGLSLType.JOPA_UINT_VECTOR_4;
		case "vec2":
			return JOPAGLSLType.JOPA_FLOAT_VECTOR_2;
		case "vec3":
			return JOPAGLSLType.JOPA_FLOAT_VECTOR_3;
		case "vec4":
			return JOPAGLSLType.JOPA_FLOAT_VECTOR_4;
		default:
			return JOPAGLSLType.JOPA_NONE;
		}
	}

	public static Color getColorForType(JOPAGLSLType type) {
		if (type == null) {
			return JOPAMain.settings.defaultPalette.portColor;
		}

		switch (type) {
		case JOPA_BOOL:
			return JOPAMain.settings.defaultPalette.boolTypeColor;
		case JOPA_INT:
			return JOPAMain.settings.defaultPalette.intTypeColor;
		case JOPA_UINT:
			return JOPAMain.settings.defaultPalette.uintTypeColor;
		case JOPA_FLOAT:
			return JOPAMain.settings.defaultPalette.floatTypeColor;
		case JOPA_BOOL_VECTOR_2:
		case JOPA_BOOL_VECTOR_3:
		case JOPA_BOOL_VECTOR_4:
		case JOPA_INT_VECTOR_2:
		case JOPA_INT_VECTOR_3:
		case JOPA_INT_VECTOR_4:
		case JOPA_UINT_VECTOR_2:
		case JOPA_UINT_VECTOR_3:
		case JOPA_UINT_VECTOR_4:
		case JOPA_FLOAT_VECTOR_2:
		case JOPA_FLOAT_VECTOR_3:
		case JOPA_FLOAT_VECTOR_4:
			return JOPAMain.settings.defaultPalette.vectorTypeColor;
		default:
			return JOPAMain.settings.defaultPalette.portColor;
		}
	}

	public static int getTypeSize(String typeName) {
		JOPAGLSLType glslType = getTypeForName(typeName);
		switch (glslType) {
		case JOPA_BOOL:
			return 1;
		case JOPA_BOOL_VECTOR_2:
			return 2;
		case JOPA_BOOL_VECTOR_3:
			return 3;
		case JOPA_BOOL_VECTOR_4:
			return 4;
		case JOPA_DOUBLE:
			return 8;
		case JOPA_FLOAT:
			return 4;
		case JOPA_FLOAT_VECTOR_2:
			return 8;
		case JOPA_FLOAT_VECTOR_3:
			return 12;
		case JOPA_FLOAT_VECTOR_4:
			return 16;
		case JOPA_INT:
			return 4;
		case JOPA_INT_VECTOR_2:
			return 8;
		case JOPA_INT_VECTOR_3:
			return 12;
		case JOPA_INT_VECTOR_4:
			return 16;
		case JOPA_UINT:
			return 4;
		case JOPA_UINT_VECTOR_2:
			return 8;
		case JOPA_UINT_VECTOR_3:
			return 12;
		case JOPA_UINT_VECTOR_4:
			return 16;
		case JOPA_VOID:
		case JOPA_NONE:
		default:
			// TODO other types
			return 0;
		}
	}

	public static String[] getAllTypes() {
		return glslTypes;
	}

}