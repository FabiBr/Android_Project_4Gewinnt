package de.ur.mi.android.excercises.starter;

public class GameWinCheck {
	Field Field;
	
	public GameWinCheck(Field field){
		this.Field = field;
	}
	

	public boolean wincheck() {
		if (vcheck() || hcheck() || dcheck())
			return true;
		return false;

	}

	private boolean dcheck() {
		// diagonal check
		// down up
		for (int i = 0; i <= 3; i++) {
			for (int j = 5; j >= 3; j--) {
				if (Field.getField(j, i) != 0
						&& Field.getField(j, i) == Field.getField(j - 1, i + 1)
						&& Field.getField(j, i) == Field.getField(j - 2, i + 2)
						&& Field.getField(j, i) == Field.getField(j - 3, i + 3))
					return true;

			}
		}
		// up down
		for (int i = 0; i <= 3; i++) {
			for (int j = 0; j <= 2; j++) {
				if (Field.getField(j, i) != 0
						&& Field.getField(j, i) == Field.getField(j + 1, i + 1)
						&& Field.getField(j, i) == Field.getField(j + 2, i + 2)
						&& Field.getField(j, i) == Field.getField(j + 3, i + 3))
					return true;

			}
		}
		return false;
	}

	private boolean hcheck() {
		// horizontal check
		for (int i = 0; i <= 3; i++) {
			for (int j = 0; j < 6; j++) {
				if (Field.getField(j, i) != 0
						&& Field.getField(j, i) == Field.getField(j, i + 1)
						&& Field.getField(j, i) == Field.getField(j, i + 2)
						&& Field.getField(j, i) == Field.getField(j, i + 3))
					return true;

			}
		}
		return false;
	}

	private boolean vcheck() {
		// vertical check
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 4; j++) {
				if (Field.getField(j, i) != 0
						&& Field.getField(j, i) == Field.getField(j + 1, i)
						&& Field.getField(j, i) == Field.getField(j + 2, i)
						&& Field.getField(j, i) == Field.getField(j + 3, i))
					return true;

			}
		}
		return false;
	}
}
