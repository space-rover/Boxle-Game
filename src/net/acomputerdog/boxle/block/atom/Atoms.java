package net.acomputerdog.boxle.block.atom;

import net.acomputerdog.boxle.block.atom.types.flow.NopAtom;
import net.acomputerdog.boxle.block.atom.types.math.DupeAtom;
import net.acomputerdog.boxle.block.atom.types.math.MultAtom;
import net.acomputerdog.boxle.block.atom.types.property.*;
import net.acomputerdog.boxle.block.atom.types.util.CreateTexAtom;
import net.acomputerdog.boxle.block.atom.types.value.PushBooleanAtom;
import net.acomputerdog.boxle.block.atom.types.value.PushFloatAtom;
import net.acomputerdog.boxle.block.atom.types.value.PushIntAtom;
import net.acomputerdog.core.storage.Registry;

public class Atoms {
    public static final Registry<Atom> ATOMS = new Registry<>();

    public static final PushFloatAtom valPush1f = ATOMS.register(new PushFloatAtom("Push 1.0f", 1f));
    public static final PushFloatAtom valPush0f = ATOMS.register(new PushFloatAtom("Push 0.0f", 0f));
    public static final PushIntAtom valPush1i = ATOMS.register(new PushIntAtom("Push 1i", 1));
    public static final PushIntAtom valPush0i = ATOMS.register(new PushIntAtom("Push 0i", 0));
    public static final PushIntAtom valPush255i = ATOMS.register(new PushIntAtom("Push 255i", 255));
    public static final PushBooleanAtom valPushTrue = ATOMS.register(new PushBooleanAtom("Push TRUE", true));
    public static final PushBooleanAtom valPushFalse = ATOMS.register(new PushBooleanAtom("Push FALSE", false));

    public static final DupeAtom mathDupe = ATOMS.register(new DupeAtom("Duplicate"));
    public static final MultAtom mathMult = ATOMS.register(new MultAtom("Multiply"));

    public static final NopAtom flowNop = ATOMS.register(new NopAtom("NOP"));

    public static final BoundsAtom propertyBounds = ATOMS.register(new BoundsAtom("Set Bounds"));
    public static final BreakableAtom propertyBreakable = ATOMS.register(new BreakableAtom("Set Breakable"));
    public static final CollidableAtom propertyCollidable = ATOMS.register(new CollidableAtom("Set Colidable"));
    public static final ExplosionResistanceAtom propertyExplosionResistance = ATOMS.register(new ExplosionResistanceAtom("Set Explosion Reistance"));
    public static final HardnessAtom propertyHardness = ATOMS.register(new HardnessAtom("Set Hardness"));
    public static final LightOutputAtom propertyLightOutput = ATOMS.register(new LightOutputAtom("Set Light Output"));
    public static final LightReductionAtom propertyLightReduction = ATOMS.register(new LightReductionAtom("Set Light Reduction"));
    public static final RenderableAtom propertyRenderable = ATOMS.register(new RenderableAtom("Set Renderable"));
    public static final ResistanceAtom propertyResistance = ATOMS.register(new ResistanceAtom("Set Resistance"));
    public static final StrengthAtom propertyStregth = ATOMS.register(new StrengthAtom("Set Strength"));
    public static final TexAtom propertyTex = ATOMS.register(new TexAtom("Set Tex"));
    public static final TransparentAtom propertyTransparent = ATOMS.register(new TransparentAtom("Set Transparent"));

    public static final CreateTexAtom utilCreateTex = ATOMS.register(new CreateTexAtom("Create Texture"));
}

