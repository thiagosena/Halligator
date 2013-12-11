package br.game.halligator.di;

import br.game.halligator.GameContext;
import roboguice.config.AbstractAndroidModule;
import roboguice.inject.SharedPreferencesName;

public class HammeringModule extends AbstractAndroidModule {

	@Override
	protected void configure() {
		bindConstant().annotatedWith(SharedPreferencesName.class).to(GameContext.PREFS_NAME);
	}
}
