package com.ctb.prism.core.transferobject;

/**
 * This class is the base Transfer Object. Other Transfer Objects should extend
 * this class
 *
 * @author TATA CONSULTANCY SERVICES
 * @version 1.0
 */
public class ObjectValueTO extends BaseTO implements Comparable<ObjectValueTO> {
	private static final long serialVersionUID = 1L;

	private String name = "";
	private String value = "";
	private String other = "";

	public ObjectValueTO() {
	}

	public ObjectValueTO(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjectValueTO other = (ObjectValueTO) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(ObjectValueTO o) {
		if (this.name == null)
			return -1;
		else
			return this.name.compareTo(o.name);
	}

}
