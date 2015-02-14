/* This file was generated by SableCC (http://www.sablecc.org/). */

package golite.node;

import golite.analysis.*;

@SuppressWarnings("nls")
public final class TAmpCaret extends Token
{
    public TAmpCaret()
    {
        super.setText("&^");
    }

    public TAmpCaret(int line, int pos)
    {
        super.setText("&^");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TAmpCaret(getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTAmpCaret(this);
    }

    @Override
    public void setText(@SuppressWarnings("unused") String text)
    {
        throw new RuntimeException("Cannot change TAmpCaret text.");
    }
}
