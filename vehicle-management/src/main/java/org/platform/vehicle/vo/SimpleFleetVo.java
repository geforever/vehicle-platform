package org.platform.vehicle.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SimpleFleetVo extends SimpleCustomerVo {

	private static final long serialVersionUID = 6288016679919129943L;

	private List<SimpleFleetVo> children = new ArrayList<SimpleFleetVo>();

	public SimpleFleetVo(Integer id, String name) {
		super(id, name);
	}

	public SimpleFleetVo(Integer id, String name, List<SimpleFleetVo> children) {
		super(id, name);
		this.children = children;
	}
}
