/*
Copyright (c) 2011, 2013 Rockwell Collins and the University of Minnesota.
Developed with the sponsorship of the Defense Advanced Research Projects Agency (DARPA).

Permission is hereby granted, free of charge, to any person obtaining a copy of this data,
including any software or models in source or binary form, as well as any drawings, specifications,
and documentation (collectively "the Data"), to deal in the Data without restriction, including
without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Data, and to permit persons to whom the Data is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Data.

THE DATA IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS, SPONSORS, DEVELOPERS, CONTRIBUTORS, OR COPYRIGHT HOLDERS BE LIABLE
FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE DATA OR THE USE OR OTHER DEALINGS IN THE DATA.
 */

package edu.umn.cs.crisys.tb.model.type;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.umn.cs.crisys.tb.TbFailure;

/* TO DO: make this a list instead of a set */

public class RecordType extends Type {
	public class RecordField {
	  public String name; 
	  public Type type;
	  RecordField(String name, Type type) {
	    this.name = name;
	    this.type = type;
	  }
	}
  
  final private List<RecordField> fields = new ArrayList<RecordField>();

	@Override
	public boolean isBaseType() {
		return false;
	}

  public boolean isEmpty() {
    return fields.isEmpty();
  }
	
	public void addField(String name, Type type) {
		fields.add(new RecordField(name, type));
	}

	public Type getField(String name) {
	  for (RecordField rf : fields) {
	    if (name.equals(rf.name)) {
	      return rf.type;
	    }
	  }
	  return null;
	}

/*	public Set<String> getFieldNames() {
		return fields.keySet();
	}
*/
	
	@Override
	public CType getCType(int indent) {
		return new CType(makeString(indent), "");
	}

	@Override
	public List<Type> dependencies() {
		ArrayList<Type> deps = new ArrayList<Type>();
		for (RecordField rf: fields) {
			deps.add(rf.type);
		}
		return deps;
	}

	public static String indent(int indent) {
	  StringBuilder outputBuffer = new StringBuilder(indent);
	  for (int i = 0; i < indent; i++){
	     outputBuffer.append(" ");
	  }
	  return outputBuffer.toString();
	}
	
	public String makeString(int indent, String tyName) {
    StringBuilder buf = new StringBuilder();
    buf.append(System.lineSeparator());
    buf.append(indent(indent)); 
    buf.append("struct " + tyName + " { " + System.lineSeparator());
    //Iterator<String> iterator = this.fields.iterator();
    for (RecordField rf: fields) {
      buf.append(indent(indent+3));
      buf.append(rf.type.getCType(indent+3).varString(rf.name));
      buf.append(" ; " + System.lineSeparator());
    }
    
    buf.append(indent(indent));
    buf.append("}");
    return buf.toString();
	}
	
	public String makeString(int indent) {
	  return makeString(indent, ""); 
	}
	
	@Override
	public String toString() {
	  return makeString(0);
	}

	@Override
	public String CTypeDecl(int indent, String name) {
    return (new CType(makeString(indent, name), "")).varString(name);
  }

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		return result;
	}

	// Equality is structural (and auto-generated by Eclipse)
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof RecordType)) {
			return false;
		}
		RecordType other = (RecordType) obj;
		if (fields == null) {
			if (other.fields != null) {
				return false;
			}
		} else if (!fields.equals(other.fields)) {
			return false;
		}
		return true;
	}
	
	// if field is 1 byte and byteAlign = 8, then pad = 7.
	// if field is 8 bytes with byteAlign = 8, then pad = 0;
	public int getOverApproximateSizeInBytes(int byteAlign) throws TbFailure {
	   int sum = 0; 
	   for (RecordField f: fields) {
	      int fieldSize = f.type.getOverApproximateSizeInBytes(byteAlign);
	      int pad = (fieldSize % byteAlign == 0) ? 0 : fieldSize - (fieldSize % byteAlign); 
	      sum += fieldSize + pad;
	   }
	   return sum;
	}
}
