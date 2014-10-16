package org.cyclopsgroup.jcli.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang.StringUtils;
import org.cyclopsgroup.caff.format.Format;
import org.cyclopsgroup.caff.format.Formats;
import org.cyclopsgroup.jcli.spi.Option;

class DefaultHelpPrinter
{
	static <T> void printHelp( AnnotationParsingContext<T> context, PrintWriter out )
	        throws Exception
	{
		DefaultHelpPrinter.printHelp(context, out, OptionHelp.class);
	}
	
    static <T, Z> void printHelp( AnnotationParsingContext<T> context, PrintWriter out, Class<Z> beanType )
        throws Exception
    {
        out.println( "[USAGE]" );
        out.println( "  " + context.cli().getName() + ( context.options().isEmpty() ? "" : " <OPTIONS>" )
            + ( context.argument() == null ? "" : " <ARGS>" ) );
        if ( StringUtils.isNotBlank( context.cli().getDescription() ) )
        {
            out.println( "[DESCRIPTION]" );
            out.println( "  " + context.cli().getDescription() );
        }
        if ( !context.options().isEmpty() )
        {
            out.println( "[OPTIONS]" );
            @SuppressWarnings("unchecked")
			Format<T> helpFormat = (Format<T>) Formats.newFixLengthFormat( beanType );
            for ( Option option : context.options() )
            {
            	Constructor<Z> c = beanType.getConstructor( Option.class );
				@SuppressWarnings("unchecked")
				String line = helpFormat.formatToString( (T) c.newInstance( option ) ).trim();
				out.println( "  " + line );
            }
        }
        if ( context.argument() != null )
        {
            out.println( "[ARGS]" );
            out.println( "  <" + context.argument().getDisplayName() + ">... " + context.argument().getDescription() );
        }
        if ( !StringUtils.isBlank( context.cli().getNote() ) )
        {
            out.println( "[NOTE]" );
            out.println( "  " + context.cli().getNote() );
        }
    }
}
