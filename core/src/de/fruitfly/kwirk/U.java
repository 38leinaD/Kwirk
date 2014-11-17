package de.fruitfly.kwirk;

public class U {
	public static float normalizeDegrees(float degrees) {
		while (degrees < 0.0f) {
			degrees += 360.0f;
		}
		while (degrees > 360.0f) {
			degrees -= 360.0f;
		}
		return degrees;
	}
}
