package com.enciyo.pinbook.reducer

import javax.inject.Singleton


@Singleton
interface StateMarker

@Singleton
abstract class RepoState : StateMarker

@Singleton
abstract class ViewState : StateMarker

@Singleton
abstract class ActionState : StateMarker

@Singleton
abstract class UserInteractions

