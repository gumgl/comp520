/* This file was generated by SableCC (http://www.sablecc.org/). */

package golite.node;

import golite.analysis.*;

@SuppressWarnings("nls")
public final class TParenR extends Token
{
    public TParenR()
    {
        super.setText(")");
    }

    public TParenR(int line, int pos)
    {
        super.setText(")");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TParenR(getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTParenR(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TParenR text.");
    }
}
