package chapter6.common;

/**
 * Special null object implementation representing the absence of a show.
 * Respects the contracts of Show.copy() and allows clients to identify an empty day.
 */
public final class NullShow implements Show {

	private static final NullShow INSTANCE = new NullShow();

	public static NullShow getInstance() {
		return INSTANCE;
	}

	private NullShow() {}

	@Override
	public String description() {
		return "No Show";
	}

	@Override
	public int runningTime() {
		return 0;
	}

	@Override
	public Show copy() {
		return this;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object pObject) {
		return pObject != null && pObject.getClass() == this.getClass();
	}

	@Override
	public String toString() {
		return "[Empty]";
	}
}
