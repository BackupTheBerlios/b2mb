//package fr.umlv.b2mb.parser;

public class IncorrectDescriptorException extends Exception
{
    public IncorrectDescriptorException()
    { super("Incorrect descriptor header"); }
    
    public IncorrectDescriptorException(String message)
    { super(message); }
}
