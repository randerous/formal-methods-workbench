package com.rockwellcollins.atc.agree;

import static jkind.lustre.parsing.LustreParseUtil.equation;
import static jkind.lustre.parsing.LustreParseUtil.expr;
import static jkind.lustre.parsing.LustreParseUtil.to;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;

import org.osate.aadl2.NamedElement;

import jkind.lustre.ArrayAccessExpr;
import jkind.lustre.ArrayExpr;
import jkind.lustre.BinaryExpr;
import jkind.lustre.BinaryOp;
import jkind.lustre.BoolExpr;
import jkind.lustre.CastExpr;
import jkind.lustre.EnumType;
import jkind.lustre.Equation;
import jkind.lustre.IfThenElseExpr;
import jkind.lustre.IntExpr;
import jkind.lustre.Location;
import jkind.lustre.NamedType;
import jkind.lustre.Node;
import jkind.lustre.NodeCallExpr;
import jkind.lustre.RealExpr;
import jkind.lustre.TypeDef;
import jkind.lustre.UnaryOp;
import jkind.lustre.VarDecl;
import jkind.lustre.builders.NodeBuilder;
import jkind.lustre.parsing.LustreParseUtil;

//Nenola = Nested Node Language
public class Nenola {

	public static class StaticState {

		public final Map<String, NodeContract> nodeContractMap;
		public final Map<String, DataContract> typeEnv;
		public final Map<String, NodeGen> globalNodeGenEnv;
		public final Map<String, DataContract> valueEnv;
		public final Map<String, NodeGen> localNodeGenEnv;
		public final Map<String, Map<String, PropVal>> props;
		public final Optional<String> currNodeConOp;

		public StaticState(Map<String, NodeContract> nodeContractMap, Map<String, DataContract> types,
				Map<String, NodeGen> globalNodeGenEnv, Map<String, DataContract> values,
				Map<String, NodeGen> localNodeGenEnv, Map<String, Map<String, PropVal>> props,
				Optional<String> currentOp) {
			this.nodeContractMap = new HashMap<>();
			this.nodeContractMap.putAll(nodeContractMap);

			this.typeEnv = new HashMap<>();
			this.typeEnv.putAll(types);

			this.globalNodeGenEnv = new HashMap<>();
			this.globalNodeGenEnv.putAll(globalNodeGenEnv);

			this.valueEnv = new HashMap<>();
			this.valueEnv.putAll(values);

			this.localNodeGenEnv = new HashMap<>();
			this.localNodeGenEnv.putAll(localNodeGenEnv);

			this.props = new HashMap<>();
			this.props.putAll(props);

			this.currNodeConOp = currentOp;

		}

		public StaticState newTypes(Map<String, DataContract> types) {
			return new StaticState(nodeContractMap, types, globalNodeGenEnv, valueEnv, localNodeGenEnv, props,
					currNodeConOp);
		}

		public StaticState newValues(Map<String, DataContract> values) {
			return new StaticState(nodeContractMap, typeEnv, globalNodeGenEnv, values, localNodeGenEnv, props,
					currNodeConOp);
		}

		public StaticState newLocalNodeGenEnv(Map<String, NodeGen> localNodeGenEnv) {
			return new StaticState(nodeContractMap, typeEnv, globalNodeGenEnv, valueEnv, localNodeGenEnv, props,
					currNodeConOp);
		}

		public StaticState newProps(Map<String, Map<String, PropVal>> props) {
			return new StaticState(nodeContractMap, typeEnv, globalNodeGenEnv, valueEnv, localNodeGenEnv, props,
					currNodeConOp);
		}

		public StaticState newCurrentOp(Optional<String> currentOp) {
			return new StaticState(nodeContractMap, typeEnv, globalNodeGenEnv, valueEnv, localNodeGenEnv, props,
					currentOp);
		}

	}

	public static enum BinRator {
		Equal, StreamCons, Implies, Equiv, Conj, Disj, NotEqual, LessThan, LessEq, GreatThan, GreatEq, Plus, Minus, Mult, Div, Mod;

		public BinaryOp toLustreRator() {
			switch (this) {
			case Equal:
				return BinaryOp.EQUAL;
			case StreamCons:
				return BinaryOp.ARROW;
			case Implies:
				return BinaryOp.IMPLIES;
			case Equiv:
				return BinaryOp.EQUAL;
			case Conj:
				return BinaryOp.AND;
			case Disj:
				return BinaryOp.OR;
			case NotEqual:
				return BinaryOp.NOTEQUAL;
			case LessThan:
				return BinaryOp.LESS;
			case LessEq:
				return BinaryOp.LESSEQUAL;
			case GreatThan:
				return BinaryOp.GREATER;
			case GreatEq:
				return BinaryOp.GREATEREQUAL;
			case Plus:
				return BinaryOp.PLUS;
			case Minus:
				return BinaryOp.MINUS;
			case Mult:
				return BinaryOp.MULTIPLY;
			case Div:
				return BinaryOp.DIVIDE;
			case Mod:
				return BinaryOp.MODULUS;
			}

			throw new RuntimeException();
		}
	}

	public static enum UniRator {
		Neg, Not, Pre;

		public UnaryOp toLustreRator() {
			switch (this) {
			case Neg:
				return UnaryOp.NEGATIVE;
			case Not:
				return UnaryOp.NOT;
			case Pre:
				return UnaryOp.PRE;
			}

			throw new RuntimeException();
		}
	}

	public static enum Tag {
		Clock, Insert, Remove, Count;

		public String toLustreString() {
			switch (this) {
			case Clock:
				return "clock";
			case Insert:
				return "insert";
			case Remove:
				return "remove";
			case Count:
				return "count";
			}

			throw new RuntimeException();
		}
	}

	public static interface Expr {

		DataContract inferDataContract(StaticState state);

		jkind.lustre.Expr toLustreExpr(StaticState state);

		jkind.lustre.Expr toLustreClockedExpr(StaticState state);

		List<jkind.lustre.VarDecl> toLustreClockedLocals(StaticState state);

		List<jkind.lustre.Equation> toLustreClockedEquations(StaticState state);

		Function<Double, Double> toDoubleFunction(String id);

		Double toDouble();

	}

	public static class TagExpr implements Expr {

		public final Expr target;
		public final Tag tag;

		public TagExpr(Expr target, Tag tag) {
			this.target = target;
			this.tag = tag;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {

			switch (this.tag) {
			case Clock:
			case Insert:
			case Remove:
				return Nenola.Prim.BoolContract;
			case Count:
				return Nenola.Prim.IntContract;
			}

			throw new RuntimeException("Error: Tag.inferDataContract");

		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			IdExpr base = (IdExpr) target;
			return new jkind.lustre.IdExpr(base.name + "_" + this.tag.toLustreString());
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class IdExpr implements Expr {
		public final String name;

		public IdExpr(String name) {
			this.name = name;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return state.valueEnv.get(this.name);
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			return new jkind.lustre.IdExpr(this.name.replace("::", "__"));
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class SelectionExpr implements Expr {
		public final Expr target;
		public final String selection;

		public SelectionExpr(Expr target, String selection) {
			this.target = target;
			this.selection = selection;

		}

		@Override
		public DataContract inferDataContract(StaticState state) {

			Contract targetContract = this.target.inferDataContract(state);
			if (targetContract instanceof RecordContract) {
				return ((RecordContract) targetContract).fields.get(this.selection);
			}

			throw new RuntimeException("Error: SelectionExpr.inferDataContract");
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			if (this.target instanceof IdExpr) {
				String base = ((IdExpr) this.target).name;

				if (state.nodeContractMap.containsKey(base)) {
					return new jkind.lustre.IdExpr(base + "__" + this.selection);
				}

			}

			return new jkind.lustre.RecordAccessExpr(this.target.toLustreExpr(state), this.selection);

		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class BinExpr implements Expr {

		public final Expr e1;
		public final BinRator rator;
		public final Expr e2;

		public BinExpr(Expr e1, BinRator rator, Expr e2) {
			this.e1 = e1;
			this.rator = rator;
			this.e2 = e2;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {

			switch (this.rator) {
			case Equal:
				return this.e1.inferDataContract(state);
			case StreamCons:
				return this.e1.inferDataContract(state);
			case Implies:
				return Nenola.Prim.BoolContract;
			case Equiv:
				return Nenola.Prim.BoolContract;
			case Conj:
				return Nenola.Prim.BoolContract;
			case Disj:
				return Nenola.Prim.BoolContract;
			case NotEqual:
				return Nenola.Prim.BoolContract;
			case LessThan:
				return Nenola.Prim.BoolContract;
			case LessEq:
				return Nenola.Prim.BoolContract;
			case GreatThan:
				return Nenola.Prim.BoolContract;
			case GreatEq:
				return Nenola.Prim.BoolContract;
			case Plus:
				return this.e1.inferDataContract(state);
			case Minus:
				return this.e1.inferDataContract(state);
			case Mult:
				return this.e1.inferDataContract(state);
			case Div:
				return this.e1.inferDataContract(state);
			case Mod:
				return this.e1.inferDataContract(state);
			}

			throw new RuntimeException();

		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			return new jkind.lustre.BinaryExpr(this.e1.toLustreExpr(state), this.rator.toLustreRator(),
					this.e2.toLustreExpr(state));
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			if (this.rator == BinRator.StreamCons) {
				return new jkind.lustre.IfThenElseExpr(new jkind.lustre.IdExpr(Lustre.initVarName),
						this.e1.toLustreClockedExpr(state), this.e2.toLustreClockedExpr(state));
			} else {
				return new BinaryExpr(this.e1.toLustreClockedExpr(state), rator.toLustreRator(),
						this.e2.toLustreClockedExpr(state));
			}
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			vars.addAll(this.e1.toLustreClockedLocals(state));
			vars.addAll(this.e2.toLustreClockedLocals(state));
			return vars;
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			List<Equation> eqs = new ArrayList<>();
			eqs.addAll(this.e1.toLustreClockedEquations(state));
			eqs.addAll(this.e2.toLustreClockedEquations(state));
			return eqs;
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {

			Double left = this.e1.toDouble();
			Double right = this.e2.toDouble();
			switch (this.rator) {
			case Plus:
				return left + right;
			case Minus:
				return left - right;
			case Mult:
				return left * right;
			case Div:
				return left / right;
			}

			throw new RuntimeException();
		}

	}

	public static class DistinctionExpr implements Expr {
		public final Expr condition;
		public final Expr trueBody;
		public final Expr falseBody;

		public DistinctionExpr(Expr condition, Expr trueBody, Expr falseBody) {
			this.condition = condition;
			this.trueBody = trueBody;
			this.falseBody = falseBody;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return this.trueBody.inferDataContract(state);
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			return new jkind.lustre.IfThenElseExpr(condition.toLustreExpr(state), trueBody.toLustreExpr(state),
					falseBody.toLustreExpr(state));
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class PrevExpr implements Expr {
		public final Expr body;
		public final Expr init;

		public PrevExpr(Expr body, Expr init) {
			this.body = body;
			this.init = init;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return this.init.inferDataContract(state);
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {

			jkind.lustre.Expr preExpr = new jkind.lustre.UnaryExpr(UnaryOp.PRE, this.body.toLustreExpr(state));

			jkind.lustre.Expr res = new BinaryExpr(this.init.toLustreExpr(state), BinaryOp.ARROW, preExpr);

			return res;
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class LocalProperty implements Expr {
		public final String propName;

		public LocalProperty(String propName) {
			this.propName = propName;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			PropVal pv = state.props.get(state.currNodeConOp.get()).get(this.propName);
			return pv.inferDataContract(state);

		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			return state.props.get(state.currNodeConOp.get()).get(propName).toLustreExpr(state);
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class ForeignProperty implements Expr {
		public final String nodeName;
		public final String propName;

		public ForeignProperty(String nodeName, String propName) {
			this.nodeName = nodeName;
			this.propName = propName;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return state.props.get(nodeName).get(propName).inferDataContract(state);
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			return state.props.get(nodeName).get(propName).toLustreExpr(state);
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class IntLit implements Expr {
		String val;

		public IntLit(String val) {
			this.val = val;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return Nenola.Prim.IntContract;
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			return new jkind.lustre.IntExpr(Integer.parseInt(val));
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			return Double.valueOf(this.val);
		}

	}

	public static class RealLit implements Expr {
		String val;

		public RealLit(String val) {
			this.val = val;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return Nenola.Prim.RealContract;
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			BigDecimal bd = BigDecimal.valueOf(Double.parseDouble(val));
			return new jkind.lustre.RealExpr(bd);
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			return Double.valueOf(this.val);
		}

	}

	public static class BoolLit implements Expr {
		boolean val;

		public BoolLit(boolean val) {
			this.val = val;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return Nenola.Prim.BoolContract;
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			return new jkind.lustre.BoolExpr(val);
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class Floor implements Expr {
		Expr arg;

		public Floor(Expr arg) {
			this.arg = arg;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return this.arg.inferDataContract(state);
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			return new CastExpr(NamedType.INT, arg.toLustreExpr(state));
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class RealCast implements Expr {
		Expr arg;

		public RealCast(Expr arg) {
			this.arg = arg;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return Nenola.Prim.RealContract;
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			return new CastExpr(NamedType.INT, arg.toLustreExpr(state));
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class Latch implements Expr {
		Expr arg;

		public Latch(Expr arg) {
			this.arg = arg;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return this.arg.inferDataContract(state);
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			jkind.lustre.IdExpr nestIdExpr = (jkind.lustre.IdExpr) arg.toLustreExpr(state);
			String latchedStr = nestIdExpr.id + "__LATCHED_";
			return new jkind.lustre.IdExpr(latchedStr);
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class Pre implements Expr {
		Expr arg;

		public Pre(Expr arg) {
			this.arg = arg;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return this.arg.inferDataContract(state);
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			return new jkind.lustre.UnaryExpr(UnaryOp.PRE, arg.toLustreExpr(state));
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class Event implements Expr {
		String arg;

		public Event(String arg) {
			this.arg = arg;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return Nenola.Prim.BoolContract;
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			return new jkind.lustre.IdExpr(arg + "___EVENT_");
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class TimeOf implements Expr {
		String arg;

		public TimeOf(String arg) {
			this.arg = arg;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return Nenola.Prim.RealContract;
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			return new jkind.lustre.IdExpr(Lustre.getTimeOfVar(arg).id);
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class TimeRise implements Expr {
		String arg;

		public TimeRise(String arg) {
			this.arg = arg;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return Nenola.Prim.BoolContract;
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			return new jkind.lustre.IdExpr(Lustre.getTimeRiseVar(arg).id);
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class TimeFall implements Expr {
		String arg;

		public TimeFall(String arg) {
			this.arg = arg;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return Nenola.Prim.RealContract;
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			return new jkind.lustre.IdExpr(Lustre.getTimeFallVar(arg).id);
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class Time implements Expr {

		public Time() {

		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return Nenola.Prim.RealContract;
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			return Lustre.timeExpr;
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class EnumLit implements Expr {
		public final String contractName;
		public final String variantName;

		public EnumLit(String contractName, String variantName) {
			this.contractName = contractName;
			this.variantName = variantName;

		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return state.typeEnv.get(contractName);
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			String typeStr = this.contractName.replace("::", "__").replace(".", "_");
			return new jkind.lustre.IdExpr(typeStr + "_" + this.variantName);
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class ArrayLit implements Expr {
		public final List<Expr> elements;

		public ArrayLit(List<Expr> elements) {
			this.elements = elements;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return new Nenola.ArrayContract("", elements.get(0).inferDataContract(state), elements.size());
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			List<jkind.lustre.Expr> elems = new ArrayList<>();
			for (Expr agreeElem : this.elements) {
				elems.add(agreeElem.toLustreExpr(state));
			}
			return new ArrayExpr(elems);
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class ArrayUpdate implements Expr {
		public final Expr arrayExpr;
		public final List<Expr> indices;
		public final List<Expr> elements;

		public ArrayUpdate(Expr arrayExpr, List<Expr> indices, List<Expr> elements) {
			this.arrayExpr = arrayExpr;
			this.indices = indices;
			this.elements = elements;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return new Nenola.ArrayContract("", elements.get(0).inferDataContract(state), elements.size());
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			jkind.lustre.Expr arrayExpr = this.arrayExpr.toLustreExpr(state);
			for (int i = 0; i < this.indices.size(); i++) {
				jkind.lustre.Expr indexExpr = this.indices.get(i).toLustreExpr(state);
				jkind.lustre.Expr newExpr = this.elements.get(i).toLustreExpr(state);
				arrayExpr = new jkind.lustre.ArrayUpdateExpr(arrayExpr, indexExpr, newExpr);

			}
			return arrayExpr;
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class RecordLit implements Expr {
		public final String contractName;
		public final Map<String, Expr> fields;

		public RecordLit(String contractName, Map<String, Expr> fields) {
			this.contractName = contractName;
			this.fields = fields;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return state.typeEnv.get(contractName);
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			Map<String, jkind.lustre.Expr> argExprMap = new HashMap<>();

			for (Entry<String, Expr> entry : this.fields.entrySet()) {
				jkind.lustre.Expr lustreExpr = entry.getValue().toLustreClockedExpr(state);
				String argName = entry.getKey();

				argExprMap.put(argName, lustreExpr);

			}

			String recName = contractName.replace("::", "__").replace(".", "_");
			return new jkind.lustre.RecordExpr(recName, argExprMap);
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class RecordUpdate implements Expr {
		public final Expr record;
		public final String selector;
		public final Expr element;

		public RecordUpdate(Expr record, String selector, Expr element) {
			this.record = record;
			this.selector = selector;
			this.element = element;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return record.inferDataContract(state);
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			return new jkind.lustre.RecordUpdateExpr(record.toLustreExpr(state), selector, element.toLustreExpr(state));
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class App implements Expr {
		public final String fnName;
		public final List<Expr> args;

		public App(String fnName, List<Expr> args) {
			this.fnName = fnName;
			this.args = args;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return Nenola.getNodeTypes(state.localNodeGenEnv.get(fnName)).get(0);
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			List<jkind.lustre.Expr> argResults = new ArrayList<>();

			for (Expr argExpr : this.args) {
				argResults.add(argExpr.toLustreExpr(state));
			}

			String lustreName = null;
			if (state.globalNodeGenEnv.containsKey(fnName)) {
				lustreName = fnName.replace("::", "__");
			} else if (state.localNodeGenEnv.containsKey(fnName)) {
				lustreName = state.currNodeConOp.get().replace("::", "__") + "__" + fnName.replace("::", "__");
			}

			return new NodeCallExpr((lustreName), argResults);
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class UnaryExpr implements Expr {
		public final UniRator rator;
		public final Expr rand;

		public UnaryExpr(UniRator rator, Expr rand) {
			this.rator = rator;
			this.rand = rand;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return rand.inferDataContract(state);
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			assert (this.rator != UniRator.Pre);

			return new jkind.lustre.UnaryExpr(this.rator.toLustreRator(), rand.toLustreExpr(state));
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {

			if (this.rator == UniRator.Pre) {

				String clockedId = Lustre.statVarPrefix + this.hashCode();
				jkind.lustre.IdExpr stateVarId = new jkind.lustre.IdExpr(clockedId);
				return stateVarId;
			} else {
				return new jkind.lustre.UnaryExpr(UnaryOp.PRE, this.rand.toLustreClockedExpr(state));
			}
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {

			if (this.rator == UniRator.Pre) {

				List<VarDecl> vars = new ArrayList<>();

				String clockedId = Lustre.statVarPrefix + this.hashCode();
				jkind.lustre.IdExpr stateVarId = new jkind.lustre.IdExpr(clockedId);
				DataContract dc = this.inferDataContract(state);
				vars.add(new VarDecl(stateVarId.id, dc.toLustreType()));

				return vars;
			} else {
				return this.rand.toLustreClockedLocals(state);

			}

		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {

			if (this.rator == UniRator.Pre) {

				List<Equation> eqs = new ArrayList<>();

				jkind.lustre.Expr preExpr = new jkind.lustre.UnaryExpr(UnaryOp.PRE,
						this.rand.toLustreClockedExpr(state));

				String clockedId = Lustre.statVarPrefix + this.hashCode();
				jkind.lustre.IdExpr stateVarId = new jkind.lustre.IdExpr(clockedId);

				jkind.lustre.Expr stateVarExpr = expr("if clk then stateVarExpr else (pre stateVar)",
						to("stateVar", stateVarId), to("stateVarExpr", preExpr), to("clk", Lustre.clockVarName));

				eqs.add(new Equation(stateVarId, stateVarExpr));
				return eqs;

			} else {
				return this.rand.toLustreClockedEquations(state);

			}

		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			if (this.rator == UniRator.Neg) {
				Double rand = this.rand.toDouble();
				return -rand;
			}

			throw new RuntimeException();
		}
	}

	public static class ArraySubExpr implements Expr {
		public final Expr array;
		public final Expr index;

		public ArraySubExpr(Expr array, Expr index) {
			this.array = array;
			this.index = index;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			ArrayContract ac = (ArrayContract) array.inferDataContract(state);
			return ac.stemContract;
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			jkind.lustre.Expr index = this.index.toLustreExpr(state);
			jkind.lustre.Expr array = this.array.toLustreExpr(state);
			return new ArrayAccessExpr(array, index);
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class IndicesExpr implements Expr {
		public final Expr array;

		public IndicesExpr(Expr array) {
			this.array = array;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			ArrayContract ac = (ArrayContract) array.inferDataContract(state);
			return new Nenola.ArrayContract("", Nenola.Prim.IntContract, ac.size);
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			Nenola.Contract arrayTypeDef = array.inferDataContract(state);

			if (arrayTypeDef instanceof Nenola.ArrayContract) {
				int size = ((Nenola.ArrayContract) arrayTypeDef).size;
				List<jkind.lustre.Expr> elems = new ArrayList<>();
				for (int i = 0; i < size; i++) {
					elems.add(new IntExpr(i));
				}

				return new ArrayExpr(elems);
			}
			throw new RuntimeException("Error caseIndicesExpr");
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class ForallExpr implements Expr {
		public final String binding;
		public final Expr array;
		public final Expr body;

		public ForallExpr(String binding, Expr array, Expr body) {
			this.binding = binding;
			this.array = array;
			this.body = body;

		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return Nenola.Prim.BoolContract;
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			jkind.lustre.Expr array = this.array.toLustreExpr(state);
			Nenola.Contract agreeType = this.array.inferDataContract(state);

			int size = 0;
			if (agreeType instanceof Nenola.ArrayContract) {
				size = ((Nenola.ArrayContract) agreeType).size;
			} else {
				throw new RuntimeException("ERROR: caseForallExpr - '" + agreeType.getClass() + "' not handled");
			}
			jkind.lustre.Expr final_expr = new BoolExpr(true);

			for (int i = 0; i < size; ++i) {
				jkind.lustre.Expr arrayAccess = new ArrayAccessExpr(array, i);
				jkind.lustre.Expr body = Lustre.substitute(this.body.toLustreExpr(state), binding, arrayAccess);
				final_expr = Lustre.makeANDExpr(final_expr, body);
			}

			return final_expr;
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class ExistsExpr implements Expr {
		public final String binding;
		public final Expr array;
		public final Expr body;

		public ExistsExpr(String binding, Expr array, Expr body) {
			this.binding = binding;
			this.array = array;
			this.body = body;

		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return Nenola.Prim.BoolContract;
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			jkind.lustre.Expr array = this.array.toLustreExpr(state);

			Nenola.Contract agreeType = this.array.inferDataContract(state);
			int size = 0;
			if (agreeType instanceof Nenola.ArrayContract) {
				size = ((Nenola.ArrayContract) agreeType).size;
			} else {
				throw new RuntimeException("ERROR: caseExistsExpr - '" + agreeType.getClass() + "' not handled");
			}
			jkind.lustre.Expr final_expr = new BoolExpr(true);

			for (int i = 0; i < size; ++i) {
				jkind.lustre.Expr arrayAccess = new ArrayAccessExpr(array, i);
				jkind.lustre.Expr body = Lustre.substitute(this.body.toLustreExpr(state), binding, arrayAccess);
				final_expr = Lustre.makeORExpr(final_expr, body);
			}

			return final_expr;
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class FlatmapExpr implements Expr {
		public final String binding;
		public final Expr array;
		public final Expr body;

		public FlatmapExpr(String binding, Expr array, Expr body) {
			this.binding = binding;
			this.array = array;
			this.body = body;

		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			Map<String, DataContract> newValues = new HashMap<>();
			newValues.putAll(state.valueEnv);
			ArrayContract ac = (ArrayContract) this.array.inferDataContract(state);
			DataContract stemType = ac.stemContract;
			newValues.put(binding, stemType);
			StaticState newState = state.newValues(newValues);
			return this.body.inferDataContract(newState);
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			Nenola.Contract agreeType = this.array.inferDataContract(state);
			int size = 0;
			if (agreeType instanceof Nenola.ArrayContract) {
				size = ((Nenola.ArrayContract) agreeType).size;
			} else {
				throw new RuntimeException("ERROR: caseFlatmapExpr");
			}
			jkind.lustre.Expr array = this.array.toLustreExpr(state);
			List<jkind.lustre.Expr> elems = new ArrayList<>();
			for (int i = 0; i < size; ++i) {
				jkind.lustre.Expr arrayAccess = new ArrayAccessExpr(array, i);
				jkind.lustre.Expr body = Lustre.substitute(this.body.toLustreExpr(state), binding, arrayAccess);

				Nenola.Contract innerArrType = this.body.inferDataContract(state);
				if (innerArrType instanceof Nenola.ArrayContract) {
					int innerSize = ((Nenola.ArrayContract) innerArrType).size;
					for (int j = 0; j < innerSize; j++) {
						jkind.lustre.Expr innerAccess = new ArrayAccessExpr(body, j);
						elems.add(innerAccess);
					}
				}

			}
			return new ArrayExpr(elems);
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class FoldLeftExpr implements Expr {
		public final String binding;
		public final Expr array;
		public final String acc;
		public final Expr initial;
		public final Expr update;

		public FoldLeftExpr(String binding, Expr array, String acc, Expr initial, Expr update) {
			this.binding = binding;
			this.array = array;
			this.acc = acc;
			this.initial = initial;
			this.update = update;

		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return this.initial.inferDataContract(state);
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			Nenola.Contract agreeType = this.array.inferDataContract(state);

			int size = 0;
			if (agreeType instanceof Nenola.ArrayContract) {
				size = ((Nenola.ArrayContract) agreeType).size;
			} else {
				throw new RuntimeException("ERROR: caseFoldLeftExpr");
			}
			jkind.lustre.Expr array = this.array.toLustreExpr(state);
			jkind.lustre.Expr accExpr = this.initial.toLustreExpr(state);
			for (int i = 0; i < size; i++) {
				jkind.lustre.Expr arrayAccess = new ArrayAccessExpr(array, i);
				accExpr = Lustre.substitute(Lustre.substitute(this.update.toLustreExpr(state), binding, arrayAccess),
						this.acc, accExpr);
			}
			return accExpr;
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static class FoldRightExpr implements Expr {
		public final String binding;
		public final Expr array;
		public final String acc;
		public final Expr initial;
		public final Expr update;

		public FoldRightExpr(String binding, Expr array, String acc, Expr initial, Expr update) {
			this.binding = binding;
			this.array = array;
			this.acc = acc;
			this.initial = initial;
			this.update = update;

		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return this.initial.inferDataContract(state);
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			Nenola.Contract agreeType = this.array.inferDataContract(state);

			int size = 0;
			if (agreeType instanceof Nenola.ArrayContract) {
				size = ((Nenola.ArrayContract) agreeType).size;
			} else {
				throw new RuntimeException("ERROR: caseFoldRightExpr");
			}
			jkind.lustre.Expr array = this.array.toLustreExpr(state);
			jkind.lustre.Expr accExpr = this.initial.toLustreExpr(state);
			for (int i = size - 1; i >= 0; i--) {
				jkind.lustre.Expr arrayAccess = new ArrayAccessExpr(array, i);
				accExpr = Lustre.substitute(Lustre.substitute(this.update.toLustreExpr(state), binding, arrayAccess),
						this.acc, accExpr);
			}
			return accExpr;
		}

		@Override
		public jkind.lustre.Expr toLustreClockedExpr(StaticState state) {
			return this.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustreClockedLocals(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustreClockedEquations(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public Function<Double, Double> toDoubleFunction(String id) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Double toDouble() {
			throw new RuntimeException();
		}

	}

	public static interface Prop {

	}

	public static class ExprProp implements Prop {
		public final Expr expr;

		public ExprProp(Expr expr) {
			this.expr = expr;
		}

	}

	public static class PatternProp implements Prop {
		public final Pattern pattern;

		public PatternProp(Pattern pattern) {
			this.pattern = pattern;
		}
	}

	public static interface Pattern {
		public jkind.lustre.Expr toLustreExprProperty(StaticState state);

		public jkind.lustre.Expr toLustreExprConstraint(StaticState state);

		public Map<String, jkind.lustre.Expr> toLustrePatternPropertyMap(StaticState state);

		public Map<String, jkind.lustre.Expr> toLustrePatternConstraintMap(StaticState state);

		public List<jkind.lustre.Expr> toLustrePatternAssertPropertyList(StaticState state);

		public List<jkind.lustre.Expr> toLustrePatternAssertConstraintList(StaticState state);

		public List<jkind.lustre.VarDecl> toLustrePatternChanInPropertyList(StaticState state);

		public List<jkind.lustre.VarDecl> toLustrePatternChanInConstraintList(StaticState state);

		public List<jkind.lustre.VarDecl> toLustrePatternChanOutPropertyList(StaticState state);

		public List<jkind.lustre.VarDecl> toLustrePatternChanOutConstraintList(StaticState state);

		public List<jkind.lustre.VarDecl> toLustrePatternChanBiPropertyList(StaticState state);

		public List<jkind.lustre.VarDecl> toLustrePatternChanBiConstraintList(StaticState state);

		public List<jkind.lustre.Equation> toLustrePatternEquationPropertyList(StaticState state);

		public List<jkind.lustre.Equation> toLustrePatternEquationConstraintList(StaticState state);

		public List<jkind.lustre.VarDecl> toLustrePatternTimeEventPropertyList(StaticState state);

		public List<jkind.lustre.VarDecl> toLustrePatternTimeEventConstraintList(StaticState state);

	}

	public static class AlwaysPattern implements Pattern {

		public final Expr expr;

		public AlwaysPattern(Expr expr) {
			this.expr = expr;
		}

		@Override
		public Map<String, jkind.lustre.Expr> toLustrePatternPropertyMap(StaticState state) {
			return new HashMap<>();
		}

		@Override
		public Map<String, jkind.lustre.Expr> toLustrePatternConstraintMap(StaticState state) {
			return new HashMap<>();
		}

		@Override
		public List<jkind.lustre.Expr> toLustrePatternAssertPropertyList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<jkind.lustre.Expr> toLustrePatternAssertConstraintList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<VarDecl> toLustrePatternChanInPropertyList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<VarDecl> toLustrePatternChanInConstraintList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<VarDecl> toLustrePatternChanOutPropertyList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<VarDecl> toLustrePatternChanOutConstraintList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<VarDecl> toLustrePatternChanBiPropertyList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<VarDecl> toLustrePatternChanBiConstraintList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustrePatternEquationPropertyList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustrePatternEquationConstraintList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public jkind.lustre.Expr toLustreExprProperty(StaticState state) {
			return this.expr.toLustreExpr(state);
		}

		@Override
		public jkind.lustre.Expr toLustreExprConstraint(StaticState state) {
			return this.expr.toLustreExpr(state);
		}

		@Override
		public List<VarDecl> toLustrePatternTimeEventPropertyList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<VarDecl> toLustrePatternTimeEventConstraintList(StaticState state) {
			return new ArrayList<>();
		}

	}

	public static class Interval {
		public final boolean lowOpen;
		public final Expr low;
		public final Expr high;
		public final boolean highOpen;

		public Interval(boolean lowOpen, Expr low, Expr high, boolean highOpen) {
			this.lowOpen = lowOpen;
			this.low = low;
			this.high = high;
			this.highOpen = highOpen;
		}
	}

	public static class WhenHoldsPattern implements Pattern {

		public final Expr causeCondition;
		public final Interval causeInterval;
		public final Expr effectEvent;
		public final boolean exclusive;
		public final Interval effectInterval;

		public WhenHoldsPattern(Expr causeCondition, Interval causeInterval, Expr effectEvent, boolean exclusive,
				Interval effectInterval) {
			this.causeCondition = causeCondition;
			this.causeInterval = causeInterval;
			this.effectEvent = effectEvent;
			this.exclusive = exclusive;
			this.effectInterval = effectInterval;

		}

		private WheneverOccursPattern toRefinementPattern(StaticState state) {
			String causeConditionString = ((jkind.lustre.IdExpr) causeCondition.toLustreExpr(state)).id;
			Expr causeEvent = new IdExpr(Lustre.getCauseHeldVar(causeConditionString).id);
			return new WheneverOccursPattern(causeEvent, effectEvent, exclusive, effectInterval);
		}

		@Override
		public Map<String, jkind.lustre.Expr> toLustrePatternPropertyMap(StaticState state) {
			return toRefinementPattern(state).toLustrePatternPropertyMap(state);
		}

		@Override
		public Map<String, jkind.lustre.Expr> toLustrePatternConstraintMap(StaticState state) {
			return toRefinementPattern(state).toLustrePatternConstraintMap(state);
		}

		private List<jkind.lustre.Expr> toLustreCauseAssertList(StaticState state) {

			List<jkind.lustre.Expr> assertList = new ArrayList<>();

			jkind.lustre.IdExpr lustreCauseCondition = (jkind.lustre.IdExpr) this.causeCondition.toLustreExpr(state);

			VarDecl causeRiseTimeVar = Lustre.getTimeRiseVar(lustreCauseCondition.id);
			VarDecl causeFallTimeVar = Lustre.getTimeFallVar(lustreCauseCondition.id);
			VarDecl causeHeldTimeoutVar = Lustre.getCauseConditionTimeOutVar(lustreCauseCondition.id);

			jkind.lustre.IdExpr causeFallTimeId = new jkind.lustre.IdExpr(causeFallTimeVar.id);
			jkind.lustre.IdExpr causeRiseTimeId = new jkind.lustre.IdExpr(causeRiseTimeVar.id);
			jkind.lustre.IdExpr causeHeldTimeoutId = new jkind.lustre.IdExpr(causeHeldTimeoutVar.id);

			jkind.lustre.Expr posRise = new jkind.lustre.BinaryExpr(causeRiseTimeId, BinaryOp.GREATER,
					new RealExpr(BigDecimal.valueOf(-1)));
			jkind.lustre.Expr gtFall = new jkind.lustre.BinaryExpr(causeRiseTimeId, BinaryOp.GREATER, causeFallTimeId);
			jkind.lustre.Expr cond = new jkind.lustre.BinaryExpr(posRise, BinaryOp.AND, gtFall);

			jkind.lustre.Expr heldTime = new BinaryExpr(causeRiseTimeId, BinaryOp.PLUS,
					this.causeInterval.high.toLustreExpr(state));
			jkind.lustre.Expr ifExpr = new IfThenElseExpr(cond, heldTime, new RealExpr(BigDecimal.valueOf(-1)));
			assertList.add(new BinaryExpr(causeHeldTimeoutId, BinaryOp.EQUAL, ifExpr));

			{

				jkind.lustre.Expr rise = new NodeCallExpr("__Rise", new jkind.lustre.IdExpr(lustreCauseCondition.id));
				jkind.lustre.Expr timeVarExpr = expr("timeRise = (if rise then time else (-1.0 -> pre timeRise))",
						to("timeRise", causeRiseTimeVar.id), to("rise", rise), to("time", Lustre.timeExpr));
				assertList.add(timeVarExpr);

				jkind.lustre.Expr lemmaExpr = expr("timeRise <= time and timeRise >= -1.0",
						to("timeRise", Lustre.timeExpr), to("time", Lustre.timeExpr));

				assertList.add(lemmaExpr);

			}

			{

				jkind.lustre.Expr Fall = new NodeCallExpr("__Fall", new jkind.lustre.IdExpr(lustreCauseCondition.id));
				jkind.lustre.Expr timeVarExpr = expr("timeFall = (if Fall then time else (-1.0 -> pre timeFall))",
						to("timeFall", causeFallTimeVar.id), to("Fall", Fall), to("time", Lustre.timeExpr));
				assertList.add(timeVarExpr);

				jkind.lustre.Expr lemmaExpr = expr("timeFall <= time and timeFall >= -1.0",
						to("timeFall", causeFallTimeVar.id), to("time", Lustre.timeExpr));

				// add this assertion to help with proofs (it should always be true)
				assertList.add(lemmaExpr);
			}
			return assertList;

		}

		@Override
		public List<jkind.lustre.Expr> toLustrePatternAssertPropertyList(StaticState state) {
			List<jkind.lustre.Expr> asserts = new ArrayList<>();
			asserts.addAll(toLustreCauseAssertList(state));
			asserts.addAll(toRefinementPattern(state).toLustrePatternAssertPropertyList(state));
			return asserts;
		}

		@Override
		public List<jkind.lustre.Expr> toLustrePatternAssertConstraintList(StaticState state) {
			List<jkind.lustre.Expr> asserts = new ArrayList<>();
			asserts.addAll(toLustreCauseAssertList(state));
			asserts.addAll(toRefinementPattern(state).toLustrePatternAssertConstraintList(state));
			return asserts;
		}

		private List<VarDecl> toLustreCauseChanInList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			jkind.lustre.IdExpr lustreCauseCondition = (jkind.lustre.IdExpr) this.causeCondition.toLustreExpr(state);
			VarDecl causeHeldTimeoutVar = Lustre.getCauseConditionTimeOutVar(lustreCauseCondition.id);
			vars.add(causeHeldTimeoutVar);
			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanInPropertyList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			vars.addAll(this.toLustreCauseChanInList(state));
			vars.addAll(this.toRefinementPattern(state).toLustrePatternChanInPropertyList(state));
			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanInConstraintList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			vars.addAll(this.toLustreCauseChanInList(state));
			vars.addAll(this.toRefinementPattern(state).toLustrePatternChanInConstraintList(state));
			return vars;
		}

		private List<VarDecl> toLustreCauseChanOutList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();

			jkind.lustre.IdExpr lustreCauseCondition = (jkind.lustre.IdExpr) this.causeCondition.toLustreExpr(state);
			{
				VarDecl causeRiseTimeVar = Lustre.getTimeRiseVar(lustreCauseCondition.id);
				vars.add(causeRiseTimeVar);
			}
			{
				VarDecl causeRiseFallVar = Lustre.getTimeFallVar(lustreCauseCondition.id);
				vars.add(causeRiseFallVar);
			}
			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanOutPropertyList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();

			vars.addAll(this.toLustreCauseChanOutList(state));
			vars.addAll(this.toRefinementPattern(state).toLustrePatternChanOutPropertyList(state));

			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanOutConstraintList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			vars.addAll(this.toLustreCauseChanOutList(state));
			vars.addAll(this.toRefinementPattern(state).toLustrePatternChanOutConstraintList(state));
			return vars;
		}

		private List<VarDecl> toLustreCauseChanBiList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			jkind.lustre.IdExpr lustreCauseCondition = (jkind.lustre.IdExpr) this.causeCondition.toLustreExpr(state);
			vars.add(Lustre.getCauseHeldVar(lustreCauseCondition.id));
			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanBiPropertyList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			vars.addAll(this.toLustreCauseChanBiList(state));
			vars.addAll(this.toRefinementPattern(state).toLustrePatternChanBiPropertyList(state));
			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanBiConstraintList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			vars.addAll(this.toLustreCauseChanBiList(state));
			vars.addAll(this.toRefinementPattern(state).toLustrePatternChanBiConstraintList(state));
			return vars;
		}

		private List<jkind.lustre.Equation> toLustreCauseEquationList(StaticState state) {
			List<jkind.lustre.Equation> equations = new ArrayList<>();
			jkind.lustre.IdExpr lustreCauseCondition = (jkind.lustre.IdExpr) this.causeCondition.toLustreExpr(state);

			VarDecl causeHeldVar = Lustre.getCauseHeldVar(lustreCauseCondition.id);
			VarDecl causeHeldTimeoutVar = Lustre.getCauseConditionTimeOutVar(lustreCauseCondition.id);
			jkind.lustre.IdExpr causeHeldId = new jkind.lustre.IdExpr(causeHeldVar.id);
			jkind.lustre.IdExpr causeHeldTimeoutId = new jkind.lustre.IdExpr(causeHeldTimeoutVar.id);

			jkind.lustre.Expr causeHeldExpr = new BinaryExpr(Lustre.timeExpr, BinaryOp.EQUAL, causeHeldTimeoutId);
			jkind.lustre.Equation equation = new jkind.lustre.Equation(causeHeldId, causeHeldExpr);
			equations.add(equation);

			return equations;
		}

		@Override
		public List<Equation> toLustrePatternEquationPropertyList(StaticState state) {
			List<Equation> equations = new ArrayList<>();
			equations.addAll(this.toLustreCauseEquationList(state));
			equations.addAll(this.toRefinementPattern(state).toLustrePatternEquationPropertyList(state));
			return equations;
		}

		@Override
		public List<Equation> toLustrePatternEquationConstraintList(StaticState state) {
			List<Equation> equations = new ArrayList<>();
			equations.addAll(this.toLustreCauseEquationList(state));
			equations.addAll(this.toRefinementPattern(state).toLustrePatternEquationConstraintList(state));
			return equations;
		}

		@Override
		public jkind.lustre.Expr toLustreExprProperty(StaticState state) {
			return this.toRefinementPattern(state).toLustreExprProperty(state);
		}

		@Override
		public jkind.lustre.Expr toLustreExprConstraint(StaticState state) {
			return this.toRefinementPattern(state).toLustreExprConstraint(state);
		}

		@Override
		public List<VarDecl> toLustrePatternTimeEventPropertyList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			VarDecl causeFallTimeVar = Lustre
					.getTimeFallVar(((jkind.lustre.IdExpr) this.causeCondition.toLustreExpr(state)).id);
			vars.add(causeFallTimeVar);
			vars.addAll(this.toRefinementPattern(state).toLustrePatternTimeEventPropertyList(state));
			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternTimeEventConstraintList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			VarDecl causeFallTimeVar = Lustre
					.getTimeFallVar(((jkind.lustre.IdExpr) this.causeCondition.toLustreExpr(state)).id);
			vars.addAll(this.toRefinementPattern(state).toLustrePatternTimeEventConstraintList(state));
			vars.add(causeFallTimeVar);
			return vars;
		}
	}

	public static class WheneverHoldsPattern implements Pattern {
		public final Expr causeEvent;
		public final Expr effectCondition;
		public final boolean exclusive;
		public final Interval interval;

		public WheneverHoldsPattern(Expr causeEvent, Expr effectCondition, boolean exclusive, Interval interval) {
			this.causeEvent = causeEvent;
			this.effectCondition = effectCondition;
			this.exclusive = exclusive;
			this.interval = interval;
		}

		private String patternIndex = this.hashCode() + "";

		@Override
		public Map<String, jkind.lustre.Expr> toLustrePatternPropertyMap(StaticState state) {
			return new HashMap<>();
		}

		@Override
		public Map<String, jkind.lustre.Expr> toLustrePatternConstraintMap(StaticState state) {
			return new HashMap<>();
		}

		@Override
		public List<jkind.lustre.Expr> toLustrePatternAssertPropertyList(StaticState state) {
			List<jkind.lustre.Expr> asserts = new ArrayList<>();
			VarDecl recordVar = Lustre.getRecordVar(patternIndex);
			asserts.addAll(Lustre.getTimeOfAsserts(recordVar.id));

			jkind.lustre.Expr expr = expr("record => cause", to("record", recordVar),
					to("cause", ((jkind.lustre.IdExpr) this.causeEvent.toLustreExpr(state)).id));
			asserts.add(expr);

			return asserts;
		}

		@Override
		public List<jkind.lustre.Expr> toLustrePatternAssertConstraintList(StaticState state) {

			List<jkind.lustre.Expr> asserts = new ArrayList<>();

			VarDecl timeCauseVar = Lustre.getTimeOfVar(((jkind.lustre.IdExpr) this.causeEvent.toLustreExpr(state)).id);
			VarDecl timeoutVar = Lustre.getTimeoutVar(patternIndex);

			jkind.lustre.Expr timeoutExpr = expr("timeout = if timeCause >= 0.0 then (timeCause + l) else -1.0",
					to("timeout", timeoutVar), to("timeCause", timeCauseVar),
					to("l", this.interval.low.toLustreExpr(state)));

			asserts.add(timeoutExpr);

			return asserts;

		}

		@Override
		public List<VarDecl> toLustrePatternChanInPropertyList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			vars.add(Lustre.getRecordVar(patternIndex));
			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanInConstraintList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<VarDecl> toLustrePatternChanOutPropertyList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			VarDecl recordVar = Lustre.getRecordVar(patternIndex);
			VarDecl timeCause = Lustre.getTimeOfVar(recordVar.id);
			vars.add(timeCause);
			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanOutConstraintList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			VarDecl timeoutVar = Lustre.getTimeoutVar(patternIndex);
			vars.add(timeoutVar);
			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanBiPropertyList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			VarDecl windowVar = Lustre.getWindowVar(patternIndex);
			vars.add(windowVar);
			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanBiConstraintList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustrePatternEquationPropertyList(StaticState state) {
			List<Equation> equations = new ArrayList<>();

			VarDecl recordVar = Lustre.getRecordVar(patternIndex);
			VarDecl windowVar = Lustre.getWindowVar(patternIndex);
			VarDecl tRecord = Lustre.getTimeOfVar(recordVar.id);

			jkind.lustre.BinaryOp left = this.interval.lowOpen ? BinaryOp.LESS : BinaryOp.LESSEQUAL;
			jkind.lustre.BinaryOp right = this.interval.highOpen ? BinaryOp.LESS : BinaryOp.LESSEQUAL;

			Equation eq = equation(
					"in_window = (trecord <> -1.0) and " + "(l + trecord " + left + " time) and (time " + right
							+ " h + trecord);",
					to("in_window", windowVar), to("trecord", tRecord), to("time", Lustre.timeExpr),
					to("l", this.interval.low.toLustreExpr(state)), to("h", this.interval.high.toLustreExpr(state)));

			equations.add(eq);
			return equations;
		}

		@Override
		public List<Equation> toLustrePatternEquationConstraintList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public jkind.lustre.Expr toLustreExprProperty(StaticState state) {
			VarDecl windowVar = Lustre.getWindowVar(patternIndex);
			return expr("in_window => effect", to("in_window", windowVar),
					to("effect", this.effectCondition.toLustreExpr(state)));
		}

		@Override
		public jkind.lustre.Expr toLustreExprConstraint(StaticState state) {
			VarDecl timeCauseVar = Lustre.getTimeOfVar(((jkind.lustre.IdExpr) this.causeEvent.toLustreExpr(state)).id);

			jkind.lustre.Expr intervalLeft = expr("timeCause + l", to("timeCause", timeCauseVar),
					to("l", this.interval.low.toLustreExpr(state)));
			jkind.lustre.Expr intervalRight = expr("timeCause + h", to("timeCause", timeCauseVar),
					to("h", this.interval.high.toLustreExpr(state)));

			jkind.lustre.BinaryOp left = this.interval.lowOpen ? BinaryOp.LESS : BinaryOp.LESSEQUAL;
			jkind.lustre.BinaryOp right = this.interval.highOpen ? BinaryOp.LESS : BinaryOp.LESSEQUAL;

			intervalLeft = new BinaryExpr(intervalLeft, left, Lustre.timeExpr);
			intervalRight = new BinaryExpr(Lustre.timeExpr, right, intervalRight);

			jkind.lustre.Expr inInterval = new BinaryExpr(intervalLeft, BinaryOp.AND, intervalRight);

			String constrString;
			if (this.exclusive) {
				constrString = "if timeCause > -1.0 and inInterval then effectTrue else not effectTrue";
			} else {
				constrString = "timeCause > -1.0 => inInterval => effectTrue";
			}

			jkind.lustre.Expr expr = expr(constrString, to("timeCause", timeCauseVar), to("inInterval", inInterval),
					to("effectTrue", this.effectCondition.toLustreExpr(state)));

			return expr;
		}

		@Override
		public List<VarDecl> toLustrePatternTimeEventPropertyList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<VarDecl> toLustrePatternTimeEventConstraintList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			VarDecl timeoutVar = Lustre.getTimeoutVar(patternIndex);
			vars.add(timeoutVar);
			return vars;
		}

	}

	public static class WheneverOccursPattern implements Pattern {
		public final Expr causeEvent;
		public final Expr effectEvent;
		public final boolean exclusive;
		public final Interval effectInterval;

		public WheneverOccursPattern(Expr causeEvent, Expr effectEvent, boolean exclusive, Interval interval) {
			this.causeEvent = causeEvent;
			this.effectEvent = effectEvent;
			this.exclusive = exclusive;
			this.effectInterval = interval;
		}

		public String patternIndex = this.hashCode() + "";

		private jkind.lustre.Expr toTimeRangeConstraint(StaticState state) {
			VarDecl effectTimeRangeVar = Lustre.getEffectTimeRangeVar(patternIndex);
			jkind.lustre.IdExpr timeRangeId = new jkind.lustre.IdExpr(effectTimeRangeVar.id);

			jkind.lustre.Expr occurs = new BinaryExpr(timeRangeId, BinaryOp.MINUS, Lustre.timeExpr);
			jkind.lustre.BinaryOp left = this.effectInterval.lowOpen ? BinaryOp.LESS : BinaryOp.LESSEQUAL;
			jkind.lustre.BinaryOp right = this.effectInterval.highOpen ? BinaryOp.LESS : BinaryOp.LESSEQUAL;

			jkind.lustre.Expr lower = new BinaryExpr(this.effectInterval.low.toLustreExpr(state), left, occurs);
			jkind.lustre.Expr higher = new BinaryExpr(occurs, right, this.effectInterval.high.toLustreExpr(state));
			return new BinaryExpr(lower, BinaryOp.AND, higher);
		}

		@Override
		public Map<String, jkind.lustre.Expr> toLustrePatternPropertyMap(StaticState state) {
			String patternIndex = Integer.toString(this.hashCode());

			jkind.lustre.IdExpr lustreCause = (jkind.lustre.IdExpr) causeEvent.toLustreExpr(state);
			jkind.lustre.IdExpr lustreEffect = (jkind.lustre.IdExpr) effectEvent.toLustreExpr(state);

			VarDecl timerVar = Lustre.getTimerVar(patternIndex);
			VarDecl runVar = Lustre.getRunningVar(patternIndex);

			VarDecl timeOfCause = Lustre.getTimeOfVar(lustreCause.id);
			VarDecl timeOfEffect = Lustre.getTimeOfVar(lustreEffect.id);

			jkind.lustre.Expr patternExpr = LustreParseUtil.expr("(timer > 0.0 => timeOfCause >= 0.0) and "
					+ "(timer <= time) and"
					+ "(timeOfEffect >= timeOfCause and timer <= high and timeOfEffect >= time - timer + low => not run) and"
					+ "(true -> (pre(timeOfEffect >= timeOfCause + low and timeOfEffect <= timeOfCause + high and timer <= high) => timer = 0.0)) and"
					+ "(timer = 0.0 or timer >= time - timeOfCause)", LustreParseUtil.to("timer", timerVar),
					LustreParseUtil.to("timeOfCause", timeOfCause), LustreParseUtil.to("timeOfEffect", timeOfEffect),
					LustreParseUtil.to("time", Lustre.timeExpr),
					LustreParseUtil.to("low", this.effectInterval.low.toLustreExpr(state)),
					LustreParseUtil.to("high", this.effectInterval.high.toLustreExpr(state)),
					LustreParseUtil.to("run", runVar));

			HashMap<String, jkind.lustre.Expr> result = new HashMap<>();
			result.put("__PATTERN__" + patternIndex, patternExpr);
			return result;

		}

		@Override
		public Map<String, jkind.lustre.Expr> toLustrePatternConstraintMap(StaticState state) {
			return new HashMap<>();
		}

		@Override
		public List<jkind.lustre.Expr> toLustrePatternAssertPropertyList(StaticState state) {

			List<jkind.lustre.Expr> assertions = new ArrayList<>();

			jkind.lustre.IdExpr causeEventExpr = ((jkind.lustre.IdExpr) this.causeEvent.toLustreExpr(state));

			VarDecl timerVar = Lustre.getTimerVar(patternIndex);
			VarDecl recordVar = Lustre.getRecordVar(patternIndex);

			jkind.lustre.IdExpr timerId = new jkind.lustre.IdExpr(timerVar.id);
			jkind.lustre.IdExpr recordId = new jkind.lustre.IdExpr(recordVar.id);

			{
				jkind.lustre.Expr expr = new jkind.lustre.BinaryExpr(timerId, BinaryOp.GREATEREQUAL,
						new RealExpr(BigDecimal.ZERO));
				assertions.add(expr);
			}

			{
				jkind.lustre.Expr causeExpr;

				if (this.effectInterval.lowOpen) {
					causeExpr = new jkind.lustre.IdExpr(causeEventExpr.id);
				} else {
					jkind.lustre.Expr eAndLZero = new jkind.lustre.BinaryExpr(
							this.effectInterval.low.toLustreExpr(state), BinaryOp.EQUAL,
							new jkind.lustre.RealExpr(BigDecimal.ZERO));
					eAndLZero = new BinaryExpr(this.effectEvent.toLustreExpr(state), BinaryOp.AND, eAndLZero);
					jkind.lustre.Expr notEAndLZero = new jkind.lustre.UnaryExpr(UnaryOp.NOT, eAndLZero);
					causeExpr = new BinaryExpr(new jkind.lustre.IdExpr(causeEventExpr.id), BinaryOp.AND, notEAndLZero);
				}
				jkind.lustre.Expr recordExpr = new BinaryExpr(recordId, BinaryOp.IMPLIES, causeExpr);
				assertions.add(recordExpr);
			}

			return assertions;
		}

		@Override
		public List<jkind.lustre.Expr> toLustrePatternAssertConstraintList(StaticState state) {

			List<jkind.lustre.Expr> assertions = new ArrayList<>();

			jkind.lustre.IdExpr causeEventExpr = ((jkind.lustre.IdExpr) this.causeEvent.toLustreExpr(state));

			VarDecl effectTimeRangeVar = Lustre.getEffectTimeRangeVar(patternIndex);
			jkind.lustre.IdExpr effectTimeRangeId = new jkind.lustre.IdExpr(effectTimeRangeVar.id);
			//
			VarDecl timeEffectVar = Lustre.getTimeWillVar(patternIndex);

			jkind.lustre.IdExpr timeEffectId = new jkind.lustre.IdExpr(timeEffectVar.id);

			jkind.lustre.Expr effectTimeRangeConstraint = this.toTimeRangeConstraint(state);
			assertions.add(effectTimeRangeConstraint);
			// make a constraint that triggers when the event WILL happen

			jkind.lustre.Expr expr = expr(
					"timeEffect = if causeId then effectTimeRangeId else (-1.0 -> pre timeEffect)",
					to("timeEffect", timeEffectId), to("causeId", causeEventExpr.id),
					to("effectTimeRangeId", effectTimeRangeId));

			assertions.add(expr);

			// a lemma that may be helpful

			jkind.lustre.Expr lemma1 = expr("timeEffect <= time + intHigh", to("timeEffect", timeEffectVar),
					to("time", Lustre.timeExpr), to("intHigh", this.effectInterval.high.toLustreExpr(state)));

			assertions.add(lemma1);

			jkind.lustre.Expr lemma2 = expr(
					"timeWill <= causeTime + high and (causeTime >= 0.0 => causeTime + low <= timeWill)",
					to("timeWill", timeEffectVar), to("causeTime", Lustre.getTimeOfVar(causeEventExpr.id)),
					to("high", this.effectInterval.high.toLustreExpr(state)),
					to("low", this.effectInterval.low.toLustreExpr(state)));

			assertions.add(lemma2);
			assertions.addAll(Lustre.getTimeOfAsserts(causeEventExpr.id));

			jkind.lustre.IdExpr lustreEffect = (jkind.lustre.IdExpr) this.effectEvent.toLustreExpr(state);

			jkind.lustre.Expr lemma3 = expr("timeWill <= time => timeWill <= timeEffect", to("timeWill", timeEffectVar),
					to("timeEffect", Lustre.getTimeOfVar(lustreEffect.id)));

			assertions.add(lemma3);
			assertions.addAll(Lustre.getTimeOfAsserts(lustreEffect.id));

			return assertions;

		}

		@Override
		public List<VarDecl> toLustrePatternChanInPropertyList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			vars.add(Lustre.getRecordVar(patternIndex));
			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanInConstraintList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			vars.add(Lustre.getEffectTimeRangeVar(patternIndex));
			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanOutPropertyList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();

			jkind.lustre.IdExpr causeEventExpr = ((jkind.lustre.IdExpr) this.causeEvent.toLustreExpr(state));
			vars.add(Lustre.getTimeOfVar(causeEventExpr.id));

			jkind.lustre.IdExpr effectEventExpr = ((jkind.lustre.IdExpr) this.effectEvent.toLustreExpr(state));
			vars.add(Lustre.getTimeFallVar(effectEventExpr.id));

			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanOutConstraintList(StaticState state) {

			List<VarDecl> vars = new ArrayList<>();
			vars.add(Lustre.getTimeWillVar(patternIndex));

			jkind.lustre.IdExpr causeEventExpr = ((jkind.lustre.IdExpr) this.causeEvent.toLustreExpr(state));

			vars.add(Lustre.getTimeOfVar(causeEventExpr.id));

			jkind.lustre.IdExpr effectEventExpr = ((jkind.lustre.IdExpr) this.effectEvent.toLustreExpr(state));
			vars.add(Lustre.getTimeFallVar(effectEventExpr.id));

			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanBiPropertyList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			vars.add(Lustre.getTimerVar(patternIndex));
			vars.add(Lustre.getRunningVar(patternIndex));
			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanBiConstraintList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustrePatternEquationPropertyList(StaticState state) {

			List<Equation> equations = new ArrayList<>();

			jkind.lustre.VarDecl timerVar = Lustre.getTimerVar(patternIndex);
			jkind.lustre.VarDecl runVar = Lustre.getRunningVar(patternIndex);
			jkind.lustre.VarDecl recordVar = Lustre.getRecordVar(patternIndex);
			jkind.lustre.IdExpr timerId = new jkind.lustre.IdExpr(timerVar.id);
			jkind.lustre.IdExpr runId = new jkind.lustre.IdExpr(runVar.id);
			jkind.lustre.IdExpr recordId = new jkind.lustre.IdExpr(recordVar.id);

			jkind.lustre.Expr preRun = new jkind.lustre.UnaryExpr(UnaryOp.PRE, runId);

			{
				jkind.lustre.Expr if2 = new IfThenElseExpr(recordId, new BoolExpr(true), preRun);
				jkind.lustre.BinaryOp left = this.effectInterval.lowOpen ? BinaryOp.LESS : BinaryOp.LESSEQUAL;
				jkind.lustre.BinaryOp right = this.effectInterval.highOpen ? BinaryOp.LESS : BinaryOp.LESSEQUAL;
				jkind.lustre.Expr timerLow = new BinaryExpr(this.effectInterval.low.toLustreExpr(state), left, timerId);
				jkind.lustre.Expr timerHigh = new BinaryExpr(timerId, right,
						this.effectInterval.high.toLustreExpr(state));
				jkind.lustre.Expr cond1 = new BinaryExpr(preRun, BinaryOp.AND, this.effectEvent.toLustreExpr(state));
				cond1 = new BinaryExpr(cond1, BinaryOp.AND, timerLow);
				cond1 = new BinaryExpr(cond1, BinaryOp.AND, timerHigh);
				jkind.lustre.Expr if1 = new IfThenElseExpr(cond1, new BoolExpr(false), if2);
				jkind.lustre.Expr runExpr = new BinaryExpr(recordId, BinaryOp.ARROW, if1);

				jkind.lustre.Equation equa = new jkind.lustre.Equation(runId, runExpr);
				equations.add(equa);
			}

			// timer = (0 -> if pre(run) then pre(timer) + (t - pre(t)) else 0)
			{
				jkind.lustre.Expr preTimer = new jkind.lustre.UnaryExpr(UnaryOp.PRE, timerId);
				jkind.lustre.Expr preT = new jkind.lustre.UnaryExpr(UnaryOp.PRE, Lustre.timeExpr);
				jkind.lustre.Expr elapsed = new BinaryExpr(Lustre.timeExpr, BinaryOp.MINUS, preT);
				jkind.lustre.Expr total = new BinaryExpr(preTimer, BinaryOp.PLUS, elapsed);
				jkind.lustre.Expr timerExpr = new IfThenElseExpr(preRun, total, new RealExpr(BigDecimal.ZERO));
				timerExpr = new BinaryExpr(new RealExpr(BigDecimal.ZERO), BinaryOp.ARROW, timerExpr);
				jkind.lustre.Equation equa = new jkind.lustre.Equation(timerId, timerExpr);
				equations.add(equa);
			}

			return equations;

		}

		@Override
		public List<Equation> toLustrePatternEquationConstraintList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public jkind.lustre.Expr toLustreExprProperty(StaticState state) {
			VarDecl timerVar = Lustre.getTimerVar(patternIndex);
			jkind.lustre.IdExpr timerId = new jkind.lustre.IdExpr(timerVar.id);

			// timer <= h
			jkind.lustre.BinaryOp right = this.effectInterval.highOpen ? BinaryOp.LESS : BinaryOp.LESSEQUAL;
			return new jkind.lustre.BinaryExpr(timerId, right, this.effectInterval.high.toLustreExpr(state));
		}

		@Override
		public jkind.lustre.Expr toLustreExprConstraint(StaticState state) {
			VarDecl timeEffectVar = Lustre.getTimeWillVar(patternIndex);
			jkind.lustre.IdExpr timeEffectId = new jkind.lustre.IdExpr(timeEffectVar.id);
			jkind.lustre.Expr timeEqualsEffectTime = new BinaryExpr(Lustre.timeExpr, BinaryOp.EQUAL, timeEffectId);
			// if the event is exclusive it only occurs when scheduled
			jkind.lustre.BinaryOp effectOp = this.exclusive ? BinaryOp.EQUAL : BinaryOp.IMPLIES;
			jkind.lustre.Expr impliesEffect = new BinaryExpr(timeEqualsEffectTime, effectOp,
					this.effectEvent.toLustreExpr(state));
			return impliesEffect;
		}

		@Override
		public List<VarDecl> toLustrePatternTimeEventPropertyList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<VarDecl> toLustrePatternTimeEventConstraintList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			vars.add(Lustre.getTimeWillVar(patternIndex));
			return vars;
		}

	}

	public static class PeriodicPattern implements Pattern {
		public final Expr event;
		public final Expr period;
		public final Optional<Expr> jitterOp;

		public PeriodicPattern(Expr event, Expr period, Optional<Expr> jitterOp) {
			this.event = event;
			this.period = period;
			this.jitterOp = jitterOp;
		}

		public String patternIndex = this.hashCode() + "";

		@Override
		public Map<String, jkind.lustre.Expr> toLustrePatternPropertyMap(StaticState state) {
			return new HashMap<>();
		}

		@Override
		public Map<String, jkind.lustre.Expr> toLustrePatternConstraintMap(StaticState state) {

			Map<String, jkind.lustre.Expr> localMap = new HashMap<>();

			String patternIndex = this.hashCode() + "";

			jkind.lustre.VarDecl periodVar = Lustre.getPeriodVar(patternIndex);
			jkind.lustre.VarDecl timeoutVar = Lustre.getTimerVar(patternIndex);

			jkind.lustre.IdExpr timeoutId = new jkind.lustre.IdExpr(timeoutVar.id);
			jkind.lustre.VarDecl timeofEvent = Lustre
					.getTimeOfVar(((jkind.lustre.IdExpr) this.event.toLustreExpr(state)).id);

			jkind.lustre.Expr jitter = this.jitterOp.isPresent() ? this.jitterOp.get().toLustreExpr(state) : null;

			jkind.lustre.Expr lemma1 = expr(
					"(timeOfEvent >= 0.0 and timeOfEvent <> time => timeout - timeOfEvent >= p - j) and "
							+ "(true -> (period <> pre(period) => period - pre(period) <= p + j)) and "
							+ "(timeOfEvent >= 0.0 => timeout - timeOfEvent <= p + j)",
					to("timeOfEvent", timeofEvent), to("time", Lustre.timeExpr), to("timeout", timeoutId),
					to("p", this.period.toLustreExpr(state)), to("j", jitter), to("period", periodVar));

			jkind.lustre.Expr lemma2 = expr("true -> timeout <> pre(timeout) => timeout - pre(timeout) >= p - j",
					to("timeout", timeoutId), to("p", this.period.toLustreExpr(state)), to("j", jitter));

			localMap.put("__PATTERN_PERIODIC__1__" + patternIndex, lemma1);
			localMap.put("__PATTERN_PERIODIC__2__" + patternIndex, lemma2);

			return localMap;
		}

		@Override
		public List<jkind.lustre.Expr> toLustrePatternAssertPropertyList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<jkind.lustre.Expr> toLustrePatternAssertConstraintList(StaticState state) {

			List<jkind.lustre.Expr> asserts = new ArrayList<>();

			VarDecl jitterVar = Lustre.getJitterVar(patternIndex);
			VarDecl periodVar = Lustre.getPeriodVar(patternIndex);
			VarDecl timeoutVar = Lustre.getTimeoutVar(patternIndex);

			jkind.lustre.IdExpr jitterId = new jkind.lustre.IdExpr(jitterVar.id);
			jkind.lustre.IdExpr periodId = new jkind.lustre.IdExpr(periodVar.id);
			jkind.lustre.IdExpr timeoutId = new jkind.lustre.IdExpr(timeoutVar.id);
			//

//			// -j <= jitter <= j
			jkind.lustre.Expr lustreJitter = this.jitterOp.isPresent() ? this.jitterOp.get().toLustreExpr(state) : null;
			jkind.lustre.Expr jitterLow = new jkind.lustre.BinaryExpr(
					new jkind.lustre.UnaryExpr(UnaryOp.NEGATIVE, lustreJitter), BinaryOp.LESSEQUAL, jitterId);
			jkind.lustre.Expr jitterHigh = new jkind.lustre.BinaryExpr(jitterId, BinaryOp.LESSEQUAL, lustreJitter);
			asserts.add(new BinaryExpr(jitterLow, BinaryOp.AND, jitterHigh));

			jkind.lustre.Expr expr = expr(
					"(0.0 <= period) and (period < p) -> " + "(period = (pre period) + (if pre(e) then p else 0.0))",
					to("period", periodVar), to("p", this.period.toLustreExpr(state)),
					to("e", this.event.toLustreExpr(state)));

			asserts.add(expr);

			// helper assertion (should be true)
			jkind.lustre.Expr lemma = expr("period - time < p - j and period >= time", to("period", periodVar),
					to("p", this.period.toLustreExpr(state)), to("time", Lustre.timeExpr), to("j", lustreJitter));

			asserts.add(lemma);
			asserts.addAll(Lustre.getTimeOfAsserts(((jkind.lustre.IdExpr) this.event.toLustreExpr(state)).id));

			// timeout = pnext + jitter
			jkind.lustre.Expr timeoutExpr = new BinaryExpr(periodId, BinaryOp.PLUS, jitterId);
			timeoutExpr = new BinaryExpr(timeoutId, BinaryOp.EQUAL, timeoutExpr);
			asserts.add(timeoutExpr);

			return asserts;
		}

		@Override
		public List<VarDecl> toLustrePatternChanInPropertyList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<VarDecl> toLustrePatternChanInConstraintList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<VarDecl> toLustrePatternChanOutPropertyList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<VarDecl> toLustrePatternChanOutConstraintList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();

			VarDecl jitterVar = Lustre.getJitterVar(patternIndex);
			vars.add(jitterVar);
			VarDecl periodVar = Lustre.getPeriodVar(patternIndex);
			vars.add(periodVar);
			VarDecl timeoutVar = Lustre.getTimeoutVar(patternIndex);
			vars.add(timeoutVar);

			VarDecl var = Lustre.getTimeOfVar(((jkind.lustre.IdExpr) this.event.toLustreExpr(state)).id);
			vars.add(var);
			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanBiPropertyList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			vars.add(Lustre.getPeriodVar(patternIndex));
			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanBiConstraintList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustrePatternEquationPropertyList(StaticState state) {

			List<Equation> eqs = new ArrayList<>();

			VarDecl periodVar = Lustre.getPeriodVar(patternIndex);

			jkind.lustre.Equation eq = equation(
					"period = if event then (if time <= P then time  else (0.0 -> pre period)) + P else (P -> pre period);",
					to("event", this.event.toLustreExpr(state)), to("period", periodVar),
					to("P", this.period.toLustreExpr(state)));

			eqs.add(eq);

			return eqs;
		}

		@Override
		public List<Equation> toLustrePatternEquationConstraintList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public jkind.lustre.Expr toLustreExprProperty(StaticState state) {
			VarDecl periodVar = Lustre.getPeriodVar(patternIndex);

			jkind.lustre.Expr lustreJitter = this.jitterOp.isPresent() ? this.jitterOp.get().toLustreExpr(state) : null;

			jkind.lustre.Expr prop = expr(
					"true -> (time >= P + j => event => (pre period) - j <= time and time <= (pre period) + j)",
					to("time", Lustre.timeExpr), to("period", periodVar), to("P", this.period.toLustreExpr(state)),
					to("j", lustreJitter), to("event", this.event.toLustreExpr(state)));
			return prop;
		}

		@Override
		public jkind.lustre.Expr toLustreExprConstraint(StaticState state) {
			VarDecl timeoutVar = Lustre.getTimeoutVar(patternIndex);

			jkind.lustre.IdExpr timeoutId = new jkind.lustre.IdExpr(timeoutVar.id);

			// event = (t = timeout)
			jkind.lustre.Expr eventExpr = new BinaryExpr(Lustre.timeExpr, BinaryOp.EQUAL, timeoutId);
			eventExpr = new BinaryExpr(this.event.toLustreExpr(state), BinaryOp.EQUAL, eventExpr);

			return eventExpr;
		}

		@Override
		public List<VarDecl> toLustrePatternTimeEventPropertyList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<VarDecl> toLustrePatternTimeEventConstraintList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			VarDecl timeoutVar = Lustre.getTimeoutVar(patternIndex);
			vars.add(timeoutVar);
			return vars;
		}
	}

	public static class SporadicPattern implements Pattern {
		public final Expr event;
		public final Expr iat;
		public final Optional<Expr> jitterOp;

		public final String patternIndex = this.hashCode() + "";

		public SporadicPattern(Expr event, Expr iat, Optional<Expr> jitterOp) {
			this.event = event;
			this.iat = iat;
			this.jitterOp = jitterOp;
		}

		@Override
		public Map<String, jkind.lustre.Expr> toLustrePatternPropertyMap(StaticState state) {
			return new HashMap<>();
		}

		@Override
		public Map<String, jkind.lustre.Expr> toLustrePatternConstraintMap(StaticState state) {
			return new HashMap<>();
		}

		@Override
		public List<jkind.lustre.Expr> toLustrePatternAssertPropertyList(StaticState state) {
			List<jkind.lustre.Expr> asserts = new ArrayList<>();
			asserts.addAll(Lustre.getTimeOfAsserts(((jkind.lustre.IdExpr) this.event.toLustreExpr(state)).id));
			return asserts;
		}

		@Override
		public List<jkind.lustre.Expr> toLustrePatternAssertConstraintList(StaticState state) {

			List<jkind.lustre.Expr> asserts = new ArrayList<>();

			VarDecl jitterVar = Lustre.getJitterVar(patternIndex);
			VarDecl periodVar = Lustre.getPeriodVar(patternIndex);
			VarDecl timeoutVar = Lustre.getTimeoutVar(patternIndex);

			jkind.lustre.IdExpr jitterId = new jkind.lustre.IdExpr(jitterVar.id);
			jkind.lustre.IdExpr periodId = new jkind.lustre.IdExpr(periodVar.id);
			jkind.lustre.IdExpr timeoutId = new jkind.lustre.IdExpr(timeoutVar.id);

			jkind.lustre.Expr lustreJitter = this.jitterOp.isPresent() ? this.jitterOp.get().toLustreExpr(state) : null;

			// -j <= jitter <= j
			jkind.lustre.Expr jitterLow = new jkind.lustre.BinaryExpr(
					new jkind.lustre.UnaryExpr(UnaryOp.NEGATIVE, lustreJitter), BinaryOp.LESSEQUAL, jitterId);
			jkind.lustre.Expr jitterHigh = new BinaryExpr(jitterId, BinaryOp.LESSEQUAL, lustreJitter);
			asserts.add(new BinaryExpr(jitterLow, BinaryOp.AND, jitterHigh));

			// pnext >= 0 -> if pre ((pnext + jitter) = t) then pnext >= p +
			// pre(pnext) else pre(pnext)

			jkind.lustre.Expr prePNext = new jkind.lustre.UnaryExpr(UnaryOp.PRE, periodId);
			jkind.lustre.Expr pNextInit = new BinaryExpr(periodId, BinaryOp.GREATEREQUAL,
					new RealExpr(BigDecimal.ZERO));
			jkind.lustre.Expr pNextCond = new BinaryExpr(periodId, BinaryOp.PLUS, jitterId);
			pNextCond = new BinaryExpr(pNextCond, BinaryOp.EQUAL, Lustre.timeExpr);
			pNextCond = new jkind.lustre.UnaryExpr(UnaryOp.PRE, pNextCond);
			jkind.lustre.Expr pNextThen = new BinaryExpr(this.iat.toLustreExpr(state), BinaryOp.PLUS, prePNext);
			pNextThen = new BinaryExpr(periodId, BinaryOp.GREATEREQUAL, pNextThen);
			jkind.lustre.Expr pNextHold = new BinaryExpr(periodId, BinaryOp.EQUAL, prePNext);
			jkind.lustre.Expr pNextIf = new IfThenElseExpr(pNextCond, pNextThen, pNextHold);
			jkind.lustre.Expr pNext = new BinaryExpr(pNextInit, BinaryOp.ARROW, pNextIf);

			asserts.add(pNext);

			// timeout = pnext + jitter
			jkind.lustre.Expr timeoutExpr = new BinaryExpr(periodId, BinaryOp.PLUS, jitterId);
			timeoutExpr = new BinaryExpr(timeoutId, BinaryOp.EQUAL, timeoutExpr);
			asserts.add(timeoutExpr);

			return asserts;
		}

		@Override
		public List<VarDecl> toLustrePatternChanInPropertyList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<VarDecl> toLustrePatternChanInConstraintList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<VarDecl> toLustrePatternChanOutPropertyList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			vars.add(Lustre.getTimeOfVar(((jkind.lustre.IdExpr) this.event.toLustreExpr(state)).id));
			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanOutConstraintList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();

			VarDecl jitterVar = Lustre.getJitterVar(patternIndex);
			vars.add(jitterVar);
			VarDecl periodVar = Lustre.getPeriodVar(patternIndex);
			vars.add(periodVar);
			VarDecl timeoutVar = Lustre.getTimeoutVar(patternIndex);
			vars.add(timeoutVar);

			VarDecl var = Lustre.getTimeOfVar(((jkind.lustre.IdExpr) this.event.toLustreExpr(state)).id);
			vars.add(var);
			return vars;
		}

		@Override
		public List<VarDecl> toLustrePatternChanBiPropertyList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<VarDecl> toLustrePatternChanBiConstraintList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustrePatternEquationPropertyList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<Equation> toLustrePatternEquationConstraintList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public jkind.lustre.Expr toLustreExprProperty(StaticState state) {
			VarDecl timeofEvent = Lustre.getTimeOfVar(((jkind.lustre.IdExpr) this.event.toLustreExpr(state)).id);

			jkind.lustre.Expr propExpr = expr(
					"(true -> (not ((pre laste) = -1.0) => event => time - (pre laste) >= period))",
					to("laste", timeofEvent), to("event", this.event.toLustreExpr(state)), to("time", Lustre.timeExpr),
					to("period", this.iat.toLustreExpr(state)));

			return propExpr;
		}

		@Override
		public jkind.lustre.Expr toLustreExprConstraint(StaticState state) {
			VarDecl timeoutVar = Lustre.getTimeoutVar(patternIndex);
			jkind.lustre.IdExpr timeoutId = new jkind.lustre.IdExpr(timeoutVar.id);
			// event = (t = timeout)
			jkind.lustre.Expr eventExpr = new BinaryExpr(Lustre.timeExpr, BinaryOp.EQUAL, timeoutId);
			eventExpr = new BinaryExpr(this.event.toLustreExpr(state), BinaryOp.EQUAL, eventExpr);

			return eventExpr;
		}

		@Override
		public List<VarDecl> toLustrePatternTimeEventPropertyList(StaticState state) {
			return new ArrayList<>();
		}

		@Override
		public List<VarDecl> toLustrePatternTimeEventConstraintList(StaticState state) {
			List<VarDecl> vars = new ArrayList<>();
			VarDecl timeoutVar = Lustre.getTimeoutVar(patternIndex);
			vars.add(timeoutVar);
			return vars;
		}

	}

	public enum SpecTag {
		Assume, Guarantee, Lemma, Assert
	}

	public static class Spec {

		public final SpecTag specTag;
		public final String name;
		public final String description;
		public final Prop prop;

		public Spec(SpecTag specTag, String name, String description, Prop prop) {
			this.specTag = specTag;
			this.name = name;
			this.description = description;
			this.prop = prop;
		}
	}

	public class Initial {
		public final Expr expr;

		public Initial(Expr expr) {
			this.expr = expr;
		}
	}

	public static class Connection {
		public final String name;
		public final Expr src;
		public final Expr dst;
		public final Optional<Expr> exprOp;
		public boolean delayed;

		public Connection(String name, Expr src, Expr dst, Optional<Expr> exprOp) {
			this.name = name;
			this.src = src;
			this.dst = dst;
			this.exprOp = exprOp;
		}

		public Equation toLustreEquation(StaticState state) {
			jkind.lustre.IdExpr dstIdExpr = (jkind.lustre.IdExpr) this.dst.toLustreExpr(state);
			return new jkind.lustre.Equation(dstIdExpr, this.src.toLustreExpr(state));
		}
	}

	public static interface TimingMode {

	}

	public static class SyncMode implements TimingMode {

		public final int v1;
		public final Optional<Integer> v2Op;

		public SyncMode(int v1, Optional<Integer> v2Op) {
			this.v1 = v1;
			this.v2Op = v2Op;
		}

	}

	public static class AsyncMode implements TimingMode {
		public AsyncMode() {

		}
	}

	public static class LatchedMode implements TimingMode {
		public LatchedMode() {

		}
	}

	public static class MNSyncMode implements TimingMode {

		public final List<String> subNodeList1;
		public final List<String> subNodeList2;
		public final List<Integer> maxList;
		public final List<Integer> minList;

		public MNSyncMode(List<String> subNodeList1, List<String> subNodeList2, List<Integer> maxList,
				List<Integer> minList) {
			this.subNodeList1 = new ArrayList<>();
			this.subNodeList1.addAll(subNodeList1);
			this.subNodeList2 = new ArrayList<>();
			this.subNodeList2.addAll(subNodeList2);
			this.maxList = new ArrayList<>();
			this.maxList.addAll(maxList);
			this.minList = new ArrayList<>();
			this.minList.addAll(minList);

		}
	}

	public static interface Contract {

		public String getName();

		public jkind.lustre.Type toLustreType();

		public boolean staticEquals(Contract other);

	}

	public static interface DataContract extends Contract {

	}

	public static enum Prim implements DataContract {
		IntContract("int", NamedType.INT), RealContract("real", NamedType.REAL), BoolContract("bool",
				NamedType.BOOL), ErrorContract("<error>", null);

		public final String name;
		public final jkind.lustre.Type lustreType;

		Prim(String name, jkind.lustre.Type lustreType) {
			this.name = name;
			this.lustreType = lustreType;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public jkind.lustre.Type toLustreType() {
			return lustreType;
		}

		@Override
		public boolean staticEquals(Contract other) {
			return this.getName().equals(other.getName());
		}

	}

	public static class RangeIntContract implements DataContract {
		public final String name;
		public final long low;
		public final long high;

		public RangeIntContract(long low, long high) {
			this.name = Prim.IntContract.name;
			this.low = low;
			this.high = high;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public jkind.lustre.Type toLustreType() {
			return NamedType.INT;
		}

		@Override
		public boolean staticEquals(Contract other) {
			return this.getName().equals(other.getName());
		}

	}

	public static class RangeRealContract implements DataContract {
		public final String name;
		public final double low;
		public final double high;

		public RangeRealContract(double f, double g) {
			this.name = Prim.RealContract.name;
			this.low = f;
			this.high = g;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public jkind.lustre.Type toLustreType() {
			return NamedType.REAL;
		}

		@Override
		public boolean staticEquals(Contract other) {
			return this.getName().equals(other.getName());
		}
	}

	public static class EnumContract implements Contract {
		private final String name;
		public final List<String> values;

		public EnumContract(String name, List<String> values) {
			this.name = name;
			this.values = new ArrayList<>();
			this.values.addAll(values);
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public jkind.lustre.Type toLustreType() {
			String lustreName = name.replace("::", "__").replace(".", "__");
			List<String> enumValues = new ArrayList<String>();
			for (String raw : values) {
				String enumValue = raw.replace("::", "__").replace(".", "__");
				enumValues.add(enumValue);
			}
			EnumType lustreEnumType = new EnumType(lustreName, enumValues);
			return lustreEnumType;
		}

		@Override
		public boolean staticEquals(Contract other) {
			return this.getName().equals(other.getName());
		}

	}

	public static interface Direc {
	}

	public static class In implements Direc {

		public In() {

		}

	}

	public static class Out implements Direc {
		public final Optional<Expr> exprOp;

		public Out(Optional<Expr> exprOp) {
			this.exprOp = exprOp;
		}
	}

	public static class Bi implements Direc {

		public Bi() {

		}

	}

	public static class RecordContract implements DataContract {

		private final String name;
		public final Map<String, DataContract> fields;

		/* reference to Xtext elm for gui update */
		public final NamedElement namedElement;

		public RecordContract(String name, Map<String, DataContract> fields, NamedElement namedElement) {
			this.name = name;
			this.fields = new HashMap<>();
			this.fields.putAll(fields);

			this.namedElement = namedElement;

		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public jkind.lustre.Type toLustreType() {
			String lustreName = name.replace("::", "__").replace(".", "__");

			Map<String, jkind.lustre.Type> lustreFields = new HashMap<>();
			for (Entry<String, DataContract> entry : fields.entrySet()) {
				String key = entry.getKey();
				jkind.lustre.Type lt = entry.getValue().toLustreType();
				if (lt != null) {
					lustreFields.put(key, lt);
				}
			}
			jkind.lustre.RecordType lustreRecType = new jkind.lustre.RecordType(lustreName, lustreFields);
			return lustreRecType;
		}

		@Override
		public boolean staticEquals(Contract other) {
			return this.getName().equals(other.getName());
		}

	}

	public static class ArrayContract implements DataContract {

		private final String name;
		public final DataContract stemContract;
		public final int size;

		public ArrayContract(String name, DataContract stemContract, int size) {
			this.name = name;
			this.size = size;
			this.stemContract = stemContract;
		}

		@Override
		public String getName() {
			return name.isEmpty() ? stemContract.getName() + "[" + size + "]" : name;
		}

		@Override
		public jkind.lustre.Type toLustreType() {

			jkind.lustre.Type lustreBaseType = stemContract.toLustreType();
			if (lustreBaseType != null) {
				jkind.lustre.ArrayType lustreArrayType = new jkind.lustre.ArrayType(lustreBaseType, size);
				return lustreArrayType;
			} else {
				return null;
			}

		}

		@Override
		public boolean staticEquals(Contract other) {
			if (other instanceof ArrayContract) {
				return size == ((ArrayContract) other).size
						&& stemContract.staticEquals(((ArrayContract) other).stemContract);
			} else {
				return false;
			}
		}

	}

	public static class Channel {
		public final String name;
		public final DataContract dataContract;
		public final Direc direction;
		public final boolean isEvent;

		public Channel(String name, DataContract dataContract, Direc direction, boolean isEvent) {
			this.name = name;
			this.dataContract = dataContract;
			this.direction = direction;
			this.isEvent = isEvent;
		}

		public VarDecl toLustreVar() {
			return new VarDecl(this.name, this.dataContract.toLustreType());
		}

	}

	public static interface PropVal {
		DataContract inferDataContract(StaticState state);

		jkind.lustre.Expr toLustreExpr(StaticState state);

	}

	public static class IntPropVal implements PropVal {
		public final long val;

		public IntPropVal(long val) {
			this.val = val;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return Prim.IntContract;
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			return new jkind.lustre.IntExpr(BigInteger.valueOf(this.val));
		}
	}

	public static class RealPropVal implements PropVal {
		public final double val;

		public RealPropVal(double val) {
			this.val = val;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return Prim.RealContract;
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			return new jkind.lustre.RealExpr(BigDecimal.valueOf(this.val));
		}
	}

	public static class NamedPropVal implements PropVal {
		public final String packageName;
		public final String name;

		public NamedPropVal(String packageName, String name) {
			this.packageName = packageName;
			this.name = name;
		}

		@Override
		public DataContract inferDataContract(StaticState state) {
			return state.props.get(packageName).get(name).inferDataContract(state);
		}

		@Override
		public jkind.lustre.Expr toLustreExpr(StaticState state) {
			PropVal pv = state.props.get(packageName).get(name);
			return pv.toLustreExpr(state);
		}
	}

	public static class NodeContract implements Contract {

		private final String name;
		public final Map<String, Channel> channels;
		public final Map<String, NodeContract> subNodes;
		public final Map<String, NodeGen> nodeGenMap;
		public final Map<String, LinearNodeGen> linearNodeGenMap;
		public final List<Connection> connections;
		public final List<Spec> specList;
		public final Optional<TimingMode> timingMode;
		public final boolean isImpl;

		/* reference to Xtext elm for gui update */
		public final NamedElement namedElement;


		public NodeContract(String name, Map<String, Channel> channels, Map<String, NodeContract> subNodes,
				Map<String, NodeGen> nodeGenMap, Map<String, LinearNodeGen> linearNodeGenMap,
				List<Connection> connections, List<Spec> specList,
				Optional<TimingMode> timingMode, boolean isImpl,
				NamedElement namedElement) {
			this.name = name;
			this.channels = new HashMap<>();
			this.channels.putAll(channels);
			this.subNodes = new HashMap<>();
			this.subNodes.putAll(subNodes);

			this.nodeGenMap = new HashMap<>();
			this.nodeGenMap.putAll(nodeGenMap);

			this.linearNodeGenMap = new HashMap<>();
			this.linearNodeGenMap.putAll(linearNodeGenMap);

			this.connections = new ArrayList<>();
			this.connections.addAll(connections);
			this.specList = specList;
			this.timingMode = timingMode;
			this.isImpl = isImpl;

			this.namedElement = namedElement;

		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public jkind.lustre.Type toLustreType() {
			String lustreName = name.replace("::", "__").replace(".", "__");

			Map<String, jkind.lustre.Type> lustreFields = new HashMap<>();
			for (Entry<String, Channel> entry : channels.entrySet()) {
				String key = entry.getKey();
				jkind.lustre.Type lt = entry.getValue().dataContract.toLustreType();
				if (lt != null) {
					lustreFields.put(key, lt);
				}
			}
			jkind.lustre.RecordType lustreRecType = new jkind.lustre.RecordType(lustreName, lustreFields);
			return lustreRecType;
		}

		@Override
		public boolean staticEquals(Contract other) {
			return this.getName().equals(other.getName());
		}

		private List<Node> toLustreClockedNodesFromNodeGens(StaticState state) {
			Map<String, DataContract> values = new HashMap<>();
			values.putAll(state.valueEnv);
			values.putAll(this.getValueTypes());
			StaticState newState = state.newValues(values).newLocalNodeGenEnv(this.nodeGenMap)
					.newCurrentOp(Optional.of(this.name));

			List<jkind.lustre.Node> lustreNodes = new ArrayList<>();
			for (NodeGen nodeGen : this.nodeGenMap.values()) {

				jkind.lustre.Node lustreNode = nodeGen.toLustreClockedNode(newState);
				lustreNodes.add(lustreNode);

			}

			return lustreNodes;
		}

		private List<Node> toLustreNodesFromNodeGens(StaticState state) {
			Map<String, DataContract> values = new HashMap<>();
			values.putAll(state.valueEnv);
			values.putAll(this.getValueTypes());
			StaticState newState = state.newValues(values).newLocalNodeGenEnv(this.nodeGenMap)
					.newCurrentOp(Optional.of(this.name));

			List<jkind.lustre.Node> lustreNodes = new ArrayList<>();
			for (NodeGen nodeGen : this.nodeGenMap.values()) {

				jkind.lustre.Node lustreNode = nodeGen.toLustreNode(newState);
				lustreNodes.add(lustreNode);

			}

			return lustreNodes;
		}

		private Map<String, DataContract> getValueTypes() {
			Map<String, DataContract> acc = new HashMap<>();
			for (Channel c : this.channels.values()) {
				acc.put(c.name, c.dataContract);
			}
			return acc;
		}


		private jkind.lustre.Node toLustreSubNode(StaticState state, boolean isMonolithic) {
			List<jkind.lustre.VarDecl> inputs = new ArrayList<>();
			List<jkind.lustre.VarDecl> locals = new ArrayList<>();
			List<jkind.lustre.Equation> equations = new ArrayList<>();
			List<jkind.lustre.Expr> assertions = this.toLustreAssertionsFromAsserts(state, isMonolithic);
			List<String> ivcs = new ArrayList<>();

			for (Entry<String, jkind.lustre.Expr> entry : this.toLustreAssumeMap(state, isMonolithic).entrySet()) {
				String inputName = entry.getKey();
				jkind.lustre.Expr expr = entry.getValue();
				inputs.add(new VarDecl(inputName, NamedType.BOOL));
				assertions.add(new BinaryExpr(new jkind.lustre.IdExpr(inputName), BinaryOp.EQUAL, expr));
			}

			for (Entry<String, jkind.lustre.Expr> entry : this.toLustreLemmaMap(state, isMonolithic).entrySet()) {
				String inputName = entry.getKey();
				jkind.lustre.Expr expr = entry.getValue();
				inputs.add(new VarDecl(inputName, NamedType.BOOL));
				assertions.add(new BinaryExpr(new jkind.lustre.IdExpr(inputName), BinaryOp.EQUAL, expr));
			}

			jkind.lustre.Expr guarConjExpr = new jkind.lustre.BoolExpr(true);
			for (Entry<String, jkind.lustre.Expr> entry : this.toLustreGuaranteeMap(state, isMonolithic).entrySet()) {
				String inputName = entry.getKey();
				jkind.lustre.Expr expr = entry.getValue();
				locals.add(new VarDecl(inputName, NamedType.BOOL));
				jkind.lustre.IdExpr guarId = new jkind.lustre.IdExpr(inputName);
				equations.add(new Equation(guarId, expr));
				ivcs.add(inputName);
				guarConjExpr = Lustre.makeANDExpr(guarId, guarConjExpr);
			}

			for (Entry<String, jkind.lustre.Expr> entry : this.toLustreLemmaMap(state, isMonolithic).entrySet()) {
				jkind.lustre.Expr expr = entry.getValue();
				guarConjExpr = Lustre.makeANDExpr(expr, guarConjExpr);
			}

			jkind.lustre.IdExpr assumHist = new jkind.lustre.IdExpr("__ASSUME__HIST");
			inputs.add(new VarDecl(assumHist.id, NamedType.BOOL));


			jkind.lustre.Expr assertExpr = new BinaryExpr(assumHist, BinaryOp.IMPLIES, guarConjExpr);
			for (jkind.lustre.Expr expr : assertions) {
				assertExpr = Lustre.makeANDExpr(expr, assertExpr);
			}

			for (Entry<String, jkind.lustre.Expr> entry : this.toLustrePatternPropMap(state, isMonolithic).entrySet()) {
				String patternVarName = entry.getKey();
				inputs.add(new VarDecl(patternVarName, NamedType.BOOL));
				jkind.lustre.Expr expr = new jkind.lustre.BinaryExpr(new jkind.lustre.IdExpr(patternVarName),
						BinaryOp.EQUAL, entry.getValue());
				assertExpr = Lustre.makeANDExpr(expr, assertExpr);
			}

			inputs.addAll(this.toLustreVarsFromInChans(state, isMonolithic));

			inputs.addAll(this.toLustreVarsFromOutChans(state, isMonolithic));

			locals.addAll(this.toLustreVarsFromBiChans(state, isMonolithic));

			equations.addAll(this.toLustreEquationsFromConnections(state, isMonolithic));

			String outputName = "__ASSERT";
			List<VarDecl> outputs = new ArrayList<>();
			outputs.add(new VarDecl(outputName, NamedType.BOOL));
			equations.add(new Equation(new jkind.lustre.IdExpr(outputName), assertExpr));

			assertions.addAll(this.toLustreAssertionsFromConnections(state));

			NodeBuilder builder = new NodeBuilder(this.getName());
			builder.addInputs(inputs);
			builder.addOutputs(outputs);
			builder.addLocals(locals);
			builder.addEquations(equations);
			builder.addIvcs(ivcs);
			Node node = builder.build();
			return node;
		}

		private List<jkind.lustre.Expr> toLustreAssertionsFromConnections(StaticState state) {

			List<jkind.lustre.Expr> acc = new ArrayList<>();
			for (Connection conn : this.connections) {
				if (conn.exprOp.isPresent()) {
					acc.add(conn.exprOp.get().toLustreExpr(state));
				} else {

					String dstName = null;
					if (conn.dst instanceof IdExpr) {
						dstName = ((IdExpr) conn.dst).name;
					} else if (conn.dst instanceof SelectionExpr) {
						Expr stem = ((SelectionExpr) conn.dst).target;
						String prefix = ((IdExpr) stem).name.replaceAll("::", "__");
						String suffix = ((IdExpr) conn.dst).name;
						dstName = prefix + "__" + suffix;


					}


					String srcName = null;
					if (conn.src instanceof IdExpr) {
						srcName = ((IdExpr) conn.src).name;
					} else if (conn.src instanceof SelectionExpr) {
						Expr stem = ((SelectionExpr) conn.src).target;
						String prefix = ((IdExpr) stem).name.replaceAll("::", "__");
						String suffix = ((IdExpr) conn.src).name;
						srcName = prefix + "__" + suffix;


					}


					jkind.lustre.Expr aadlConnExpr;

					if (!conn.delayed) {
						aadlConnExpr = new jkind.lustre.BinaryExpr(new jkind.lustre.IdExpr(srcName),
								jkind.lustre.BinaryOp.EQUAL, new jkind.lustre.IdExpr(dstName));
					} else {
						// we need to get the correct type for the aadlConnection
						// we can assume that the source and destination types are
						// the same at this point

						DataContract srcType = conn.src.inferDataContract(state);
						jkind.lustre.Expr initExpr = Lustre.getInitValueFromType(srcType.toLustreType());
						jkind.lustre.Expr preSource = new jkind.lustre.UnaryExpr(UnaryOp.PRE,
								new jkind.lustre.IdExpr(srcName));
						jkind.lustre.Expr srcExpr = new jkind.lustre.BinaryExpr(initExpr, BinaryOp.ARROW, preSource);
						aadlConnExpr = new jkind.lustre.BinaryExpr(srcExpr, BinaryOp.EQUAL,
								new jkind.lustre.IdExpr(dstName));
					}

					acc.add(aadlConnExpr);

				}
			}
			return acc;
		}

//		public Node toLustreNode(boolean isMain, boolean isMonolithic) {
//
//			if (this.lustreNodeCache.containsKey(isMonolithic)) {
//				return lustreNodeCache.get(isMonolithic);
//			}
//
//			Node node = null;
//			if (isMain && this.isImpl) {
//				node = this.toLustreMainNode(isMonolithic);
//			} else {
//				node = this.toLustreSubNode(isMonolithic);
//			}
//
//			lustreNodeCache.put(isMonolithic, node);
//			return node;
//
//		}

		private Node toLustreMainNode(StaticState state, boolean isMonolithic) {
			List<jkind.lustre.Expr> assertions = new ArrayList<>();
			List<VarDecl> locals = new ArrayList<>();
			List<VarDecl> inputs = new ArrayList<>();
			List<Equation> equations = new ArrayList<>();
			List<String> properties = new ArrayList<>();
			List<String> ivcs = new ArrayList<>();


			for (Entry<String, jkind.lustre.Expr> entry : this.toLustreAssumeMap(state, isMonolithic).entrySet()) {

				String inputName = entry.getKey();
				jkind.lustre.Expr assumeExpr = entry.getValue();
				locals.add(new VarDecl(inputName, NamedType.BOOL));
				jkind.lustre.IdExpr idExpr = new jkind.lustre.IdExpr(inputName);
				equations.add(new Equation(idExpr, assumeExpr));
				assertions.add(idExpr);
				ivcs.add(inputName);
			}

			for (jkind.lustre.Expr assertion : this.toLustreAssertionsFromAsserts(state, isMonolithic)) {
				assertions.add(assertion);
			}

			// add assumption and monolithic lemmas first (helps with proving)
			for (VarDecl var : this.toLustreVarsFromOutChans(state, isMonolithic)) {
				inputs.add(var);
			}

			for (String propStr : this.toLustreStringPropertyList(state, isMonolithic)) {
				properties.add(propStr);
			}

			// add property that all assumption history is true
			jkind.lustre.Expr assumeConj = new BoolExpr(true);
			for (Entry<String,NodeContract> entry : this.subNodes.entrySet()) {

				String id = entry.getKey() + Lustre.assumeHistVar;
				assumeConj = new jkind.lustre.BinaryExpr(new jkind.lustre.IdExpr(id), jkind.lustre.BinaryOp.AND,
						assumeConj);
			}

			VarDecl assumeHistVar = Lustre.assumeHistVar;
			locals.add(assumeHistVar);
			equations.add(new Equation(new jkind.lustre.IdExpr(assumeHistVar.id), assumeConj));
			properties.add(assumeHistVar.id);


			for (Entry<String, jkind.lustre.Expr> entry : this.toLustrePatternPropMap(state, isMonolithic).entrySet()) {
				String name = entry.getKey();
				jkind.lustre.Expr expr = entry.getValue();
				locals.add(new VarDecl(name, NamedType.BOOL));
				equations.add(new Equation(new jkind.lustre.IdExpr(name), expr));
				properties.add(name);
			}

			for (Entry<String, jkind.lustre.Expr> entry : this.toLustreLemmaMap(state, isMonolithic).entrySet()) {
				String name = entry.getKey();
				jkind.lustre.Expr expr = entry.getValue();
				locals.add(new VarDecl(name, NamedType.BOOL));
				equations.add(new Equation(new jkind.lustre.IdExpr(name), expr));
				properties.add(name);
			}

			for (Entry<String, jkind.lustre.Expr> entry : this.toLustreGuaranteeMap(state, isMonolithic).entrySet()) {
				String name = entry.getKey();
				jkind.lustre.Expr expr = entry.getValue();
				locals.add(new VarDecl(name, NamedType.BOOL));
				equations.add(new Equation(new jkind.lustre.IdExpr(name), expr));
				properties.add(name);
			}

			for (VarDecl var : this.toLustreVarsFromInChans(state, isMonolithic)) {
				inputs.add(var);
			}
			for (VarDecl var : this.toLustreVarsFromBiChans(state, isMonolithic)) {
				locals.add(var);
			}



			equations.addAll(this.toLustreEquationsFromConnections(state, isMonolithic));
			assertions.add(Lustre.getTimeConstraint(this.toEventTimeVarList(state, isMonolithic)));

			NodeBuilder builder = new NodeBuilder("main");
			builder.addInputs(inputs);
			builder.addLocals(locals);
			builder.addEquations(equations);
			builder.addProperties(properties);
			builder.addAssertions(assertions);
			builder.addIvcs(ivcs);

			Node main = builder.build();
			return main;
		}

		private List<String> toLustreStringPropertyList(StaticState state, boolean isMonolithic) {
			List<String> strs = new ArrayList<>();

			for (Spec spec : this.specList) {

				if (spec.prop instanceof PatternProp) {
					Pattern pattern = ((PatternProp) spec.prop).pattern;
					for (VarDecl var : pattern.toLustrePatternTimeEventPropertyList(state)) {
						strs.add(var.id);
					}
				}

			}

			return strs;
		}

		private List<jkind.lustre.VarDecl> toEventTimeVarList(StaticState state, boolean isMonolithic) {

			List<jkind.lustre.VarDecl> vars = new ArrayList<>();

			for (Spec spec : this.specList) {

				if (spec.prop instanceof PatternProp) {
					Pattern pattern = ((PatternProp) spec.prop).pattern;
					List<jkind.lustre.VarDecl> localList = this.isProperty(isMonolithic, spec.specTag)
							? pattern.toLustrePatternTimeEventPropertyList(state)
							: pattern.toLustrePatternTimeEventConstraintList(state);

					vars.addAll(localList);
				}

			}

			for (Entry<String, NodeContract> entry : this.subNodes.entrySet()) {

				String prefix = entry.getKey() + "__";
				NodeContract nc = entry.getValue();
				for (VarDecl subTimeVar : nc.toEventTimeVarList(state, isMonolithic)) {
					vars.add(new VarDecl(prefix + subTimeVar.id, subTimeVar.type));
				}
			}

			return vars;
		}


		private List<Equation> toLustreEquationsFromConnections(StaticState state, boolean isMonolithic) {
			List<Equation> equations = new ArrayList<>();

			for (Connection conn : this.connections) {
				equations.add(conn.toLustreEquation(state));
			}

			for (Spec spec : this.specList) {

				if (spec.prop instanceof PatternProp) {
					Pattern pattern = ((PatternProp) spec.prop).pattern;

					List<jkind.lustre.Equation> localList = this.isProperty(isMonolithic, spec.specTag)
							? pattern.toLustrePatternEquationPropertyList(state)
							: pattern.toLustrePatternEquationConstraintList(state);
					equations.addAll(localList);
				}

			}
			return equations;
		}

		private List<VarDecl> toLustreVarsFromBiChans(StaticState state, boolean isMonolithic) {

			List<VarDecl> vars = new ArrayList<>();

			for (Channel chan : this.channels.values()) {
				if (chan.direction instanceof Bi) {
					vars.add(chan.toLustreVar());
				}
			}

			for (Spec spec : this.specList) {

				if (spec.prop instanceof PatternProp) {
					Pattern pattern = ((PatternProp) spec.prop).pattern;

					List<jkind.lustre.VarDecl> localList = this.isProperty(isMonolithic, spec.specTag)
							? pattern.toLustrePatternChanBiPropertyList(state)
							: pattern.toLustrePatternChanBiConstraintList(state);
					vars.addAll(localList);
				}

			}

			return vars;
		}


		private List<VarDecl> toLustreVarsFromOutChans(StaticState state, boolean isMonolithic) {

			List<VarDecl> vars = new ArrayList<>();
			for (Channel chan : this.channels.values()) {
				if (chan.direction instanceof Out) {
					vars.add(chan.toLustreVar());
				}
			}

			for (Spec spec : this.specList) {

				if (spec.prop instanceof PatternProp) {
					Pattern pattern = ((PatternProp) spec.prop).pattern;

					List<jkind.lustre.VarDecl> localList = this.isProperty(isMonolithic, spec.specTag)
							? pattern.toLustrePatternChanOutPropertyList(state)
							: pattern.toLustrePatternChanOutConstraintList(state);
					vars.addAll(localList);
				}

			}

			for (Entry<String, NodeContract> entry : this.subNodes.entrySet()) {
				String prefix = entry.getKey();
				NodeContract nc = entry.getValue();

				for (VarDecl nestedVar : nc.toLustreVarsFromOutChans(state, isMonolithic)) {
					String id = prefix + "__" + nestedVar.id;
					jkind.lustre.Type type = nestedVar.type;
					vars.add(new VarDecl(id, type));
				}

				for (String assumeKey : nc.toLustreAssumeMap(state, isMonolithic).keySet()) {
					String id = prefix + "__" + assumeKey;
					vars.add(new VarDecl(id, NamedType.BOOL));
				}

				for (String propKey : nc.toLustreLemmaMap(state, isMonolithic).keySet()) {
					String id = prefix + "__" + propKey;
					vars.add(new VarDecl(id, NamedType.BOOL));
				}

				for (String propKey : nc.toLustrePatternPropMap(state, isMonolithic).keySet()) {
					String id = prefix + "__" + propKey;
					vars.add(new VarDecl(id, NamedType.BOOL));
				}

				vars.add(new VarDecl(prefix + "__ASSUME__HIST", NamedType.BOOL));

			}

			return vars;
		}

		private List<VarDecl> toLustreVarsFromInChans(StaticState state, boolean isMonolithic) {

			List<VarDecl> vars = new ArrayList<>();
			for (Channel chan : this.channels.values()) {
				if (chan.direction instanceof In) {
					vars.add(chan.toLustreVar());
				}
			}

			for (Spec spec : this.specList) {

				if (spec.prop instanceof PatternProp) {
					Pattern pattern = ((PatternProp) spec.prop).pattern;

					List<jkind.lustre.VarDecl> localList = this.isProperty(isMonolithic, spec.specTag)
							? pattern.toLustrePatternChanInPropertyList(state)
							: pattern.toLustrePatternChanInConstraintList(state);
					vars.addAll(localList);
				}

			}

			for (Entry<String, NodeContract> entry : this.subNodes.entrySet()) {
				String prefix = entry.getKey();
				NodeContract nc = entry.getValue();
				for (VarDecl nestedVar : nc.toLustreVarsFromInChans(state, isMonolithic)) {
					String id = prefix + "__" + nestedVar.id;
					jkind.lustre.Type type = nestedVar.type;
					vars.add(new VarDecl(id, type));
				}

				vars.add(nc.toLustreClockVar());


			}

			return vars;
		}


		private VarDecl toLustreClockVar() {
			return new jkind.lustre.VarDecl(this.getName() + "__CLOCK_", jkind.lustre.NamedType.BOOL);
		}


		private boolean isProperty(boolean isMonolithic, SpecTag specTag) {
			return (specTag == SpecTag.Assume && !this.isImpl && !isMonolithic)
					|| (specTag == SpecTag.Lemma && (this.isImpl || isMonolithic))
					|| (specTag == SpecTag.Guarantee && (this.isImpl || isMonolithic));

		}


		private Map<String, jkind.lustre.Expr> toLustrePatternPropMap(StaticState state, boolean isMonolithic) {

			Map<String, jkind.lustre.Expr> props = new HashMap<>();
			for (Spec spec : this.specList) {

				if (spec.prop instanceof PatternProp) {
					Pattern pattern = ((PatternProp) spec.prop).pattern;

					Map<String, jkind.lustre.Expr> localMap = this.isProperty(isMonolithic, spec.specTag)
							? pattern.toLustrePatternPropertyMap(state)
							: pattern.toLustrePatternConstraintMap(state);
					props.putAll(localMap);
				}
			}

			for (Entry<String, NodeContract> entry : this.subNodes.entrySet()) {
				String prefix = entry.getKey();
				NodeContract nc = entry.getValue();

				for (Entry<String, jkind.lustre.Expr> nestedEntry : nc.toLustrePatternPropMap(state, isMonolithic)
						.entrySet()) {
					String key = prefix + "__" + nestedEntry.getKey();
					jkind.lustre.Expr expr = nestedEntry.getValue();
					props.put(key, expr);
				}

			}

			return props;
		}

		private List<jkind.lustre.Expr> toLustreAssertionsFromAsserts(StaticState state, boolean isMonolithic) {

			List<jkind.lustre.Expr> exprs = new ArrayList<>();

			for (Spec spec : this.specList) {
				if (spec.specTag == SpecTag.Assert) {

					if (spec.prop instanceof PatternProp) {

						Pattern pattern = ((PatternProp) spec.prop).pattern;

						jkind.lustre.Expr expr = this.isProperty(isMonolithic, spec.specTag)
								? pattern.toLustreExprProperty(state)
								: pattern.toLustreExprConstraint(state);
						exprs.add(expr);
					} else if (spec.prop instanceof ExprProp) {
						jkind.lustre.Expr expr = ((ExprProp) spec.prop).expr.toLustreExpr(state);
						exprs.add(expr);
					}
				}

				if (spec.prop instanceof PatternProp) {
					Pattern pattern = ((PatternProp) spec.prop).pattern;

					List<jkind.lustre.Expr> localList = this.isProperty(isMonolithic, spec.specTag)
							? pattern.toLustrePatternAssertPropertyList(state)
							: pattern.toLustrePatternAssertConstraintList(state);
					exprs.addAll(localList);
				}

			}

			for (Entry<String, NodeContract> entry : this.subNodes.entrySet()) {

				NodeContract nc = entry.getValue();

				for (jkind.lustre.Expr subAssert : nc.toLustreAssertionsFromAsserts(state, isMonolithic)) {
					exprs.add(subAssert);
				}

				String prefix = entry.getKey() + "__";
				jkind.lustre.IdExpr timeId = Lustre.timeExpr;

				exprs.add(new jkind.lustre.BinaryExpr(timeId, BinaryOp.EQUAL,
						new jkind.lustre.IdExpr(prefix + timeId.id)));


			}

			return exprs;
		}


		private Map<String, jkind.lustre.Expr> toLustreGuaranteeMap(StaticState state, boolean isMonolithic) {

			Map<String, jkind.lustre.Expr> exprMap = new HashMap<>();
			int suffix = 0;
			for (Spec spec : this.specList) {

				if (spec.specTag == SpecTag.Guarantee) {
					String key = SpecTag.Guarantee.name() + "__" + suffix;
					if (spec.prop instanceof PatternProp) {
						Pattern pattern = ((PatternProp) spec.prop).pattern;
						jkind.lustre.Expr expr = this.isProperty(isMonolithic, spec.specTag)
								? pattern.toLustreExprProperty(state)
								: pattern.toLustreExprConstraint(state);
						exprMap.put(key, expr);

					} else if (spec.prop instanceof ExprProp) {
						jkind.lustre.Expr expr = ((ExprProp) spec.prop).expr.toLustreExpr(state);
						exprMap.put(key, expr);

					}

					suffix = suffix + 1;
				}

			}

			return exprMap;
		}


		private Map<String, jkind.lustre.Expr> toLustreLemmaMap(StaticState state, boolean isMonolithic) {

			Map<String, jkind.lustre.Expr> exprMap = new HashMap<>();
			int suffix = 0;
			for (Spec spec : this.specList) {

				if (spec.specTag == SpecTag.Lemma) {
					String key = SpecTag.Lemma.name() + "__" + suffix;

					if (spec.prop instanceof PatternProp) {
						Pattern pattern = ((PatternProp) spec.prop).pattern;
						jkind.lustre.Expr expr = this.isProperty(isMonolithic, spec.specTag)
								? pattern.toLustreExprProperty(state)
								: pattern.toLustreExprConstraint(state);
						exprMap.put(key, expr);

					} else if (spec.prop instanceof ExprProp) {
						jkind.lustre.Expr expr = ((ExprProp) spec.prop).expr.toLustreExpr(state);
						exprMap.put(key, expr);

					}

					suffix = suffix + 1;
				}

			}

			return exprMap;
		}


		private Map<String, jkind.lustre.Expr> toLustreAssumeMap(StaticState state, boolean isMonolithic) {

			Map<String, jkind.lustre.Expr> exprMap = new HashMap<>();
			int suffix = 0;
			for (Spec spec : this.specList) {

				if (spec.specTag == SpecTag.Assume) {
					String key = SpecTag.Assume.name() + "__" + suffix;
					if (spec.prop instanceof PatternProp) {
						Pattern pattern = ((PatternProp) spec.prop).pattern;
						jkind.lustre.Expr expr = this.isProperty(isMonolithic, spec.specTag)
								? pattern.toLustreExprProperty(state)
								: pattern.toLustreExprConstraint(state);
						exprMap.put(key, expr);

					} else if (spec.prop instanceof ExprProp) {
						jkind.lustre.Expr expr = ((ExprProp) spec.prop).expr.toLustreExpr(state);
						exprMap.put(key, expr);

					}

					suffix = suffix + 1;
				}

			}

			return exprMap;
		}

		public List<Node> toLustreSubNodes(StaticState state, boolean isMonolithic) {

			List<Node> nodes = new ArrayList<>();
			for (NodeContract subNodeContract : this.subNodes.values()) {
				nodes.add(this.toLustreSubNode(state, isMonolithic));
				nodes.addAll(subNodeContract.toLustreSubNodes(state, isMonolithic));
			}

			return nodes;

		}

		public List<Node> toLustreNodesFromLinearNodeGens(StaticState state) {
			Map<String, DataContract> values = new HashMap<>();
			values.putAll(state.valueEnv);
			values.putAll(this.getValueTypes());
			StaticState newState = state.newValues(values).newLocalNodeGenEnv(this.nodeGenMap)
					.newCurrentOp(Optional.of(this.name));

			List<jkind.lustre.Node> lustreNodes = new ArrayList<>();
			for (LinearNodeGen linearNodeGen : this.linearNodeGenMap.values()) {
				jkind.lustre.Node lustreNode = linearNodeGen.toLustreNode(newState);
				lustreNodes.add(lustreNode);

			}

			return lustreNodes;
		}

		public List<Node> toLustreClockedNodesFromLinearNodeGens(StaticState state) {
			Map<String, DataContract> values = new HashMap<>();
			values.putAll(state.valueEnv);
			values.putAll(this.getValueTypes());
			StaticState newState = state.newValues(values).newLocalNodeGenEnv(this.nodeGenMap)
					.newCurrentOp(Optional.of(this.name));

			List<jkind.lustre.Node> lustreNodes = new ArrayList<>();
			for (LinearNodeGen linearNodeGen : this.linearNodeGenMap.values()) {

				jkind.lustre.Node lustreNode = linearNodeGen.toLustreClockedNode(newState);
				lustreNodes.add(lustreNode);

			}

			return lustreNodes;
		}

	}


	public static boolean staticEqual(Contract t1, Contract t2) {
		return t1.staticEquals(t2);
	}

	public static class DataFlow {
		public final List<String> tgts;
		public final Expr src;

		public DataFlow(List<String> tgts, Expr src) {
			this.tgts = tgts;
			this.src = src;
		}

		public DataFlow(String tgt, Expr src) {
			this.tgts = Collections.singletonList(tgt);
			this.src = src;
		}

		public Equation toLustreEquation(StaticState state) {

			jkind.lustre.Expr expr = src.toLustreExpr(state);
			List<jkind.lustre.IdExpr> ids = new ArrayList<>();
			for (String tgt : tgts) {
				ids.add(new jkind.lustre.IdExpr(tgt));
			}
			return new Equation(ids, expr);
		}
	}

	public static class NodeGen {

		public final String name;
		public final Map<String, Channel> channels;
		public final List<DataFlow> dataFlows;
		public final List<String> properties;

		public NodeGen(String name, Map<String, Channel> channels, List<DataFlow> dataFlows, List<String> properties) {
			this.name = name;
			this.channels = channels;
			this.dataFlows = dataFlows;
			this.properties = properties;
		}

		public Node toLustreNode(StaticState state) {

			List<VarDecl> inputs = this.toLustreInputlList();
			List<VarDecl> outputs = this.toLustreOutputlList();
			List<VarDecl> internals = this.toLustreLocallList();
			List<Equation> eqs = this.toLustreEquations(state);
			List<String> props = this.properties;

			NodeBuilder builder = new NodeBuilder(this.name);
			builder.addInputs(inputs);
			builder.addOutputs(outputs);
			builder.addLocals(internals);
			builder.addEquations(eqs);
			builder.addProperties(props);

			return builder.build();
		}


		private List<Equation> toLustreEquations(StaticState state) {
			List<Equation> equations = new ArrayList<>();
			for (DataFlow df : this.dataFlows) {
				equations.add(df.toLustreEquation(state));
			}
			return equations;
		}

		private List<VarDecl> toLustreInputlList() {
			List<VarDecl> acc = new ArrayList<>();
			for (Channel c : this.channels.values()) {
				if (c.direction instanceof Nenola.In) {
					VarDecl var = new VarDecl(c.name, c.dataContract.toLustreType());
					acc.add(var);
				}
			}
			return acc;
		}

		private List<VarDecl> toLustreOutputlList() {

			List<VarDecl> acc = new ArrayList<>();
			for (Channel c : this.channels.values()) {
				if (c.direction instanceof Nenola.Out) {
					VarDecl var = new VarDecl(c.name, c.dataContract.toLustreType());
					acc.add(var);

				}
			}
			return acc;
		}

		private List<VarDecl> toLustreLocallList() {

			List<VarDecl> acc = new ArrayList<>();
			for (Channel c : this.channels.values()) {
				if (c.direction instanceof Nenola.Bi) {
					VarDecl var = new VarDecl(c.name, c.dataContract.toLustreType());
					acc.add(var);
				}
			}
			return acc;
		}

		private Optional<Node> clockedNodeMap = Optional.empty();

		public Node toLustreClockedNode(StaticState state) {
			if (!clockedNodeMap.isPresent()) {

				NodeBuilder builder = new NodeBuilder(this.toLustreNode(state));
				builder.setId(Lustre.clockedNodePrefix + this.toLustreNode(state).id);
				builder.clearEquations();
				builder.clearInputs();
				builder.addInput(new VarDecl(Lustre.clockVarName, NamedType.BOOL));
				builder.addInput(new VarDecl(Lustre.initVarName, NamedType.BOOL));
				builder.addInputs(this.toLustreNode(state).inputs);


				for (DataFlow df : this.dataFlows) {
//
					jkind.lustre.Expr clockedExpr = df.src.toLustreClockedExpr(state);
					List<jkind.lustre.IdExpr> idExprs = new ArrayList<>();
					for (String id : df.tgts) {
						idExprs.add(new jkind.lustre.IdExpr(id));
					}
					// this will make an unguarded pre expression, but any non initialized
					// outputs should be masked by the init expression in the calling agree node
					jkind.lustre.Expr lustreExpr = new jkind.lustre.IfThenElseExpr(
							new jkind.lustre.IdExpr(Lustre.clockVarName), clockedExpr,
							new jkind.lustre.UnaryExpr(UnaryOp.PRE, idExprs.get(0)));
					builder.addEquation(new Equation(idExprs, lustreExpr));
					builder.addLocals(df.src.toLustreClockedLocals(state));
					builder.addEquations(df.src.toLustreClockedEquations(state));

				}
				return builder.build();
			} else {
				return clockedNodeMap.get();
			}
		}

	}

	// A line segment is represented by Cartesian coordinates of its endpoints
	public static class Segment {
		public double startX, startY, stopX, stopY;

		public Segment(double startX, double startY, double stopX, double stopY) {
			this.startX = startX;
			this.startY = startY;
			this.stopX = stopX;
			this.stopY = stopY;
		}
	}

	public static class SegmentPair {
		public final Segment lower;
		public final Segment upper;

		public SegmentPair(Segment lower, Segment upper) {
			this.lower = lower;
			this.upper = upper;
		}
	}

	// Distance holds the distance the non-linear function extends
	// below and above a straight line through its endpoints
	public static class Distance {

		public double below, above;

		public Distance(double below, double above) {
			this.below = below;
			this.above = above;
		}

		public Distance(Function<Double, Double> f, double start, double stop) {
			int checks = 100000; // number of points to check between start and stop
									// positions

			double distAbove = 0.0;
			double distBelow = 0.0;
			double dist = 0.0;
			double step = (stop - start) / checks;
			double x = start;
			double y = f.apply(start);
			double slope = (f.apply(stop) - f.apply(start)) / (stop - start);
			while (x < stop) {
				dist = f.apply(x) - (y + slope * (x - start));
				if (dist > distAbove) {
					distAbove = dist;
				}
				if (dist < distBelow) {
					distBelow = dist;
				}
				x = x + step;
			}
			this.below = distBelow;
			this.above = distAbove;

		}
	}

	private static List<SegmentPair> linearize(Function<Double, Double> f, double start, double stop,
			double bound) {

		List<SegmentPair> acc = new ArrayList<>();

		Distance dist = new Distance(f, start, stop);

		if (dist.above < bound & dist.below > -bound) {
			// Found lower and upper segments whose distance lies within the
			// acceptable bounds
			acc.add(new SegmentPair(new Segment(start, f.apply(start) + dist.below, stop, f.apply(stop) + dist.below),
					new Segment(start, f.apply(start) + dist.above, stop, f.apply(stop) + dist.above)));
		} else {
			// Bound is exceeded. Divide in half and try again.
			double mid = (start + stop) / 2.0;

			acc.addAll(linearize(f, start, mid, bound));
			acc.addAll(linearize(f, mid, stop, bound));
		}

		return acc;
	}

	private static Expr generateAgreeLinearBoundImplicationExpr(IdExpr inputIdExpr, IdExpr resultIdExpr,
			BinRator rator, Segment seg) {

		RealLit inputMinExpr = new RealLit(Double.toString(seg.startX));

		RealLit inputMaxExpr = new RealLit(Double.toString(seg.stopX));

		RealLit resultOriginExpr = new RealLit(Double.toString(seg.startY));

		RealLit resultSlopeExpr = new RealLit(Double.toString((seg.stopY - seg.startY) / (seg.stopX - seg.startX)));

		BinExpr rangeMinExpr = new BinExpr(inputIdExpr, BinRator.GreatEq, inputMinExpr);

		BinExpr rangeMaxExpr = new BinExpr(inputIdExpr, BinRator.LessEq, inputMaxExpr);

		BinExpr rangeExpr = new BinExpr(rangeMinExpr, BinRator.Conj, rangeMaxExpr);

		BinExpr shiftExpr = new BinExpr(inputIdExpr, BinRator.Minus, inputMinExpr);

		BinExpr multiplyExpr = new BinExpr(resultSlopeExpr, BinRator.Mult, shiftExpr);

		BinExpr additionExpr = new BinExpr(resultOriginExpr, BinRator.Plus, multiplyExpr);

		BinExpr linearBoundExpr = new BinExpr(resultIdExpr, rator, additionExpr);

		BinExpr result = new BinExpr(rangeExpr, BinRator.Implies, linearBoundExpr);

		return result;
	}


	public static class LinearNodeGen {

		public final String name;
		public final String argName;
		public final Expr start;
		public final Expr stop;
		public final Expr bound;
		public final Expr body;

		public LinearNodeGen(String name, String argName, Expr start, Expr stop, Expr bound, Expr body) {
			this.name = name;
			this.argName = argName;
			this.start = start;
			this.stop = stop;
			this.bound = bound;
			this.body = body;
		}

		private NodeGen toNodeGen() {


			Map<String, Channel> channels = new HashMap<>();
			List<DataFlow> dataFlows = new ArrayList<>();
			List<String> properties = new ArrayList<>();

			String nodeName = "__constraint__" + name;

			Channel inChan = new Nenola.Channel("inp", Nenola.Prim.RealContract, new Nenola.In(), false);
			channels.put(inChan.name, inChan);
			Channel resultChan = new Nenola.Channel("result", Nenola.Prim.RealContract,
					new Nenola.In(),
					false);
			channels.put(resultChan.name, resultChan);
			Channel constraintChan = new Nenola.Channel("constraint", Nenola.Prim.BoolContract,
					new Nenola.Out(Optional.empty()), false);
			channels.put(constraintChan.name, constraintChan);

			Function<Double, Double> f = this.body.toDoubleFunction(this.argName);

			Double startDouble = this.start.toDouble();
			Double stopDouble = this.stop.toDouble();
			Double boundDouble = this.bound.toDouble();

			List<SegmentPair> segPairList = linearize(f, startDouble, stopDouble, boundDouble);

			IdExpr inputIdExpr = new Nenola.IdExpr(inChan.name);

			IdExpr resultIdExpr = new Nenola.IdExpr(resultChan.name);
			RealLit domainCheckLowerLit = new RealLit(Double.toString(segPairList.get(0).lower.startX));

			BinExpr domainCheckLowerExpr = new Nenola.BinExpr(domainCheckLowerLit, Nenola.BinRator.LessEq,
					inputIdExpr);

			RealLit domainCheckUpperLit = new RealLit(
					Double.toString(segPairList.get(segPairList.size() - 1).lower.stopX));

			BinExpr domainCheckUpperExpr = new Nenola.BinExpr(inputIdExpr, Nenola.BinRator.LessEq,
					domainCheckUpperLit);

			BinExpr domainCheckExpr  = new Nenola.BinExpr(domainCheckLowerExpr, Nenola.BinRator.Conj,
					domainCheckUpperExpr);


			BoolLit trueLitExpr = new BoolLit(true);

			Expr upperBoundExpr = trueLitExpr;
			for (SegmentPair pair : segPairList) {
				BinExpr andExpr = new BinExpr(upperBoundExpr, BinRator.Conj, generateAgreeLinearBoundImplicationExpr(inputIdExpr, resultIdExpr, BinRator.LessEq, pair.upper));
				upperBoundExpr = andExpr;
			}


			Expr lowerBoundExpr = trueLitExpr;
			for (SegmentPair pair : segPairList) {
				BinExpr andExpr = new BinExpr(lowerBoundExpr, BinRator.Conj, generateAgreeLinearBoundImplicationExpr(
						inputIdExpr, resultIdExpr, BinRator.GreatEq, pair.lower));
				lowerBoundExpr = andExpr;
			}

			BinExpr boundsCheckExpr = new BinExpr(upperBoundExpr, BinRator.Conj, lowerBoundExpr);
			BinExpr constraintExpr = new BinExpr(domainCheckExpr, BinRator.Conj, boundsCheckExpr);


			DataFlow constraintEq = new DataFlow(constraintChan.name, constraintExpr);

			dataFlows.add(constraintEq);

			String domainCheckLemmaName = resultChan.name + "__domain__check";
			DataFlow domainCheckEq = new DataFlow(domainCheckLemmaName, domainCheckExpr);
			dataFlows.add(domainCheckEq);
			properties.add(domainCheckLemmaName);

			return new NodeGen(nodeName, channels, dataFlows, properties);


		}

		private jkind.lustre.Node toLustreNode(StaticState state) {
			return this.toNodeGen().toLustreNode(state);
		}

		private jkind.lustre.Node toLustreClockedNode(StaticState state) {
			return this.toNodeGen().toLustreClockedNode(state);
		}

	}

	private static List<Node> lustreNodesFromMain(StaticState state, NodeContract main, boolean isMonolithic) {
		List<Node> nodes = new ArrayList<>();
		Node mainNode = main.toLustreMainNode(state, isMonolithic);
		nodes.add(mainNode);
		List<Node> subs = main.toLustreSubNodes(state, isMonolithic);
		nodes.addAll(subs);
		return nodes;
	}

	private static List<DataContract> getNodeTypes(NodeGen ng) {

		List<DataContract> nodeTypes = new ArrayList<>();
		for (Channel c : ng.channels.values()) {
			if (c.direction instanceof Nenola.Out) {
				nodeTypes.add(c.dataContract);
			}
		}
		return nodeTypes;
	}

	public static class Program {
		public final NodeContract main;
		public final Map<String, NodeContract> nodeContractMap;
		public final Map<String, DataContract> types;
		public final Map<String, NodeGen> nodeGenMap;
		public final Map<String, LinearNodeGen> linearNodeGenMap;
		public final Map<String, Map<String, PropVal>> propMap;

		public Program(NodeContract main, Map<String, NodeContract> nodeContractMap,
				Map<String, DataContract> types, Map<String, NodeGen> nodeGenMap,
				Map<String, LinearNodeGen> linearNodeGenMap,
				Map<String, Map<String, PropVal>> propMap) {
			this.main = main;
			this.nodeContractMap = nodeContractMap;
			this.types = types;
			this.nodeGenMap = nodeGenMap;
			this.linearNodeGenMap = linearNodeGenMap;
			this.propMap = propMap;
		}

		public Map<String, jkind.lustre.Program> toRecursiveLustrePrograms() {
			// TODO Auto-generated method stub
			return null;
		}

		public Map<String, jkind.lustre.Program> toRealizabilityLustrePrograms() {
			// TODO Auto-generated method stub
			return null;
		}

		public Map<String, jkind.lustre.Program> toMonolithicLustrePrograms(boolean usingKind2) {
			Map<String, jkind.lustre.Program> programMap = this.toConsistencyPrograms();

			if (usingKind2) {
				programMap.putAll(this.toContractPrograms());
			} else {
				programMap.putAll(this.toAssumeGuaranteePrograms(true));
			}

			return programMap;
		}

		public Map<String, jkind.lustre.Program> toSingleLustrePrograms() {
			Map<String, jkind.lustre.Program> programMap = this.toConsistencyPrograms();
			programMap.putAll(this.toAssumeGuaranteePrograms(false));
			return programMap;
		}

		private Map<String, jkind.lustre.Program> toContractPrograms() {
			// TODO Auto-generated method stub
			return null;
		}

		private Map<String, jkind.lustre.Program> toAssumeGuaranteePrograms(boolean isMonolithic) {

			StaticState state = new StaticState(this.nodeContractMap, this.types, this.nodeGenMap, new HashMap<>(),
					new HashMap<>(),
					propMap, Optional.empty());
			List<jkind.lustre.TypeDef> lustreTypes = this.lustreTypesFromDataContracts();
			List<jkind.lustre.Node> lustreNodes = new ArrayList<>();
			lustreNodes.addAll(this.toLustreNodesFromNodeGens(state));
			lustreNodes.addAll(this.toLustreClockedNodesFromNodeGens(state));

			lustreNodes.addAll(this.toLustreNodesFromLinearNodeGens(state));
			lustreNodes.addAll(this.toLustreClockedNodesFromLinearNodeGens(state));

			lustreNodes.addAll(Nenola.lustreNodesFromMain(state, main, isMonolithic));

			lustreNodes.add(Lustre.getHistNode());
			lustreNodes.addAll(Lustre.getRealTimeNodes());
			jkind.lustre.Program program = new jkind.lustre.Program(Location.NULL, lustreTypes, null, null, lustreNodes,
					main.getName());
			Map<String, jkind.lustre.Program> programs = new HashMap<>();
			programs.put("Contract Guarantees", program);
			return programs;
		}



		private List<Node> toLustreClockedNodesFromLinearNodeGens(StaticState state) {
			List<jkind.lustre.Node> lustreNodes = new ArrayList<>();
			for (LinearNodeGen nodeGen : this.linearNodeGenMap.values()) {

				jkind.lustre.Node lustreNode = nodeGen.toLustreClockedNode(state);
				lustreNodes.add(lustreNode);

			}

			for (Entry<String, NodeContract> entry : this.nodeContractMap.entrySet()) {

				NodeContract nc = entry.getValue();

				lustreNodes.addAll(nc.toLustreClockedNodesFromLinearNodeGens(state));

			}

			return lustreNodes;
		}

		private List<Node> toLustreNodesFromLinearNodeGens(StaticState state) {
			List<jkind.lustre.Node> lustreNodes = new ArrayList<>();
			for (LinearNodeGen nodeGen : this.linearNodeGenMap.values()) {

				jkind.lustre.Node lustreNode = nodeGen.toLustreNode(state);
				lustreNodes.add(lustreNode);

			}

			for (Entry<String, NodeContract> entry : this.nodeContractMap.entrySet()) {

				NodeContract nc = entry.getValue();

				lustreNodes.addAll(nc.toLustreNodesFromLinearNodeGens(state));

			}

			return lustreNodes;
		}

		private List<Node> toLustreClockedNodesFromNodeGens(StaticState state) {
			List<jkind.lustre.Node> lustreNodes = new ArrayList<>();
			for (NodeGen nodeGen : this.nodeGenMap.values()) {

				jkind.lustre.Node lustreNode = nodeGen.toLustreClockedNode(state);
				lustreNodes.add(lustreNode);

			}

			for (Entry<String, NodeContract> entry : this.nodeContractMap.entrySet()) {

				NodeContract nc = entry.getValue();

				lustreNodes.addAll(nc.toLustreClockedNodesFromNodeGens(state));

			}

			return lustreNodes;
		}

		private List<Node> toLustreNodesFromNodeGens(StaticState state) {
			List<jkind.lustre.Node> lustreNodes = new ArrayList<>();
			for (NodeGen nodeGen : this.nodeGenMap.values()) {

				jkind.lustre.Node lustreNode = nodeGen.toLustreNode(state);
				lustreNodes.add(lustreNode);

			}

			for (Entry<String, NodeContract> entry : this.nodeContractMap.entrySet()) {

				NodeContract nc = entry.getValue();

				lustreNodes.addAll(nc.toLustreNodesFromNodeGens(state));

			}

			return lustreNodes;
		}

		private List<TypeDef> lustreTypesFromDataContracts() {
			List<jkind.lustre.TypeDef> lustreTypes = new ArrayList<>();
			for (Entry<String, DataContract> entry : this.types.entrySet()) {

				String name = entry.getKey();
				Contract contract = entry.getValue();
				jkind.lustre.Type lustreType = contract.toLustreType();
				jkind.lustre.TypeDef lustreTypeDef = new jkind.lustre.TypeDef(name, lustreType);
				lustreTypes.add(lustreTypeDef);

			}

			return lustreTypes;
		}


		private Map<String, jkind.lustre.Program> toConsistencyPrograms() {
			// TODO Auto-generated method stub

			return null;
		}

	}



}