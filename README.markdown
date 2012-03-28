# Scalatron Bot Toolkit

This project contains a toolkit for building bots for the excellent Scalatron (http://scalatron.wordpress.com/) game.

## Overview
The Scalatron game is an application for teaching Scala programming. Players implement their own 'bot' and deploy it to the game server. The bot implementation needs to respond to a simple string command and answer with a number of command actions.

The protocol is very simple and easy to understand, but the game allows an almost infinite complexity in the playing strategies that can be developed.

## The Toolkit
The toolkit abstracts away the parsing of commands and the producing of action strings, allowing the developer to focus on writing actual strategy objects.

Each strategy is implemented as a PartialFunction that matches on a Command object and returns a sequence of actions to carry out. Multiple strategies can be chained together to implement the full bot behaviour.

The toolkit is configuration driven. The strategies used and the order they are matched is defined in configuration. Additionally, each strategy can be further configured from the same configuration file.

## Building New Strategies

TODO

## Configuration

TODO
