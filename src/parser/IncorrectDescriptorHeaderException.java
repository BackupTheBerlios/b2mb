//package fr.umlv.b2mb.parser;

public class IncorrectDescriptorHeaderException extends Exception
{
    public IncorrectDescriptorHeaderException()
    { super("Incorrect descriptor header"); }
    
    public IncorrectDescriptorHeaderException(String message)
    { super(message); }
}
