/* This file was generated by SableCC (http://www.sablecc.org/). */

package golite.node;

import golite.analysis.*;

@SuppressWarnings("nls")
public final class TLitOctal extends Token
{
    public TLitOctal(String text)
    {
        setText(text);
    }

    public TLitOctal(String text, int line, int pos)
    {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TLitOctal(getText(), getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTLitOctal(this);
    }
}
