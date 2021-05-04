package jopa.util;

import java.awt.Color;

import jopa.main.JOPAMain;
import jopa.types.JOPAGLSLType;

public final class JOPATypeUtil {

	public static String getNameForType(JOPAGLSLType type) {
		if (type == null) {
			return null;
		}

		switch (type) {
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
			return JOPAMain.settings.defaultPalette.boolTypeColor;
		case JOPA_FLOAT:
			return JOPAMain.settings.defaultPalette.boolTypeColor;
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
	
}