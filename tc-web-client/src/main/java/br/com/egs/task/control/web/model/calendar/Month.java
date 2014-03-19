package br.com.egs.task.control.web.model.calendar;

public enum Month {
	Jan(0, "Janeiro"),
    Fev(1, "Fevereiro"),
    Mar(2, "Março"),
    Abr(3, "Abril"),
    Mai(4, "Maio"),
    Jun(5, "Junho"),
    Jul(6, "Julho"),
    Ago(7, "Agosto"),
    Set(8, "Setembro"),
    Out(9, "Outubro"),
    Nov(10, "Novembro"),
    Dez(11, "Dezembro");

	private int id;
    private String fullName;

	Month(int id, String fullName) {
		this.id = id;
        this.fullName = fullName;
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

    public String getFullName() {
        return fullName;
    }
}
