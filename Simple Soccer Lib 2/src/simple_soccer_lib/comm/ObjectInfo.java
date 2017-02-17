package simple_soccer_lib.comm;

public class ObjectInfo {
	public String m_type;
	public float m_distance;
	public float m_direction;
	public float m_distChange;
	public float m_dirChange;

	// Initialization member functions
	public ObjectInfo(String type) {
		m_type = type;
	}

	public float getDistance() {
		return m_distance;
	}

	public float getDirection() {
		return m_direction;
	}

	public float getDistChange() {
		return m_distChange;
	}

	public float getDirChange() {
		return m_dirChange;
	}

	public String getType() {
		return m_type;
	}

	@Override
	public String toString() {
		return "ObjectInfo [m_type=" + m_type + ", m_distance=" + m_distance + ", m_direction=" + m_direction
				+ ", m_distChange=" + m_distChange + ", m_dirChange=" + m_dirChange + "]";
	}
}
