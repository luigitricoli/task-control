package br.com.egs.task.control.web.model.calendar;

public enum Month {
	Jan(0), Fev(1), Mar(2), Abr(3), Mai(4), Jun(5), Jul(6), Ago(7), Set(8), Out(9), Nov(10), Dez(11);

	private int id;

	Month(int id) {
		this.id = id;
	}

	public static Month getByNumber(int number) {
		int id = number - 1;
		return Month.values()[id];
	}

	public static Month getById(int id) {
		return Month.values()[id];
	}

	public Month getNext() {
		return Month.values()[id + 1];
	}

	public int getId() {
		return id;
	}

}
