package jopa.util;

public final class JOPACast {

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
	
}