package br.game.halligator.di;

import java.util.List;

import roboguice.application.RoboApplication;

import com.google.inject.Module;

public final class HammeringApplication extends RoboApplication {
	
	@Override
	protected void addApplicationModules(List<Module> modules) {
		modules.add(new HammeringModule());
    }
}
