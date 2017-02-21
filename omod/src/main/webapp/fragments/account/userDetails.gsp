<div class="ke-panel-frame">
	<div class="ke-panel-heading">Login Details</div>

	<% if (user) { %>
	<div class="ke-panel-content">
		<% if (user.retired) { %>
		<div class="ke-warning" style="margin-bottom: 5px">${ ui.message("chaiemr.loginIsDisabled") }</div>
		<% } %>

		${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Username", value: user.username ]) }
		${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Roles", value: user.roles.join(", ") ]) }

		<%
			def inheritedRoles = user.allRoles
			inheritedRoles.removeAll(user.roles)
		%>

		<% if (inheritedRoles) { %>
		${ ui.includeFragment("chaiui", "widget/dataPoint", [ label: "Inherited", value: inheritedRoles.join(", ") ]) }
		<% } %>
	</div>
	<% } %>

	<div class="ke-panel-footer">
	<% if (user && !user.retired) { %>

		<%= ui.includeFragment("chaiui", "widget/dialogForm", [
				buttonConfig: [
						label: "Edit",
						iconProvider: "chaiui",
						icon: "glyphs/edit.png"
				],
				dialogConfig: [ heading: "Edit login details for ${ chaiui.formatPersonName(person) }", width: 90, height: 90 ],
				fragment: "account/userDetails",
				fragmentProvider: "chaiemr",
				action: "submit",
				prefix: "user",
				commandObject: form,
				hiddenProperties: [ "userId" ],
				properties: [ "username", "password", "confirmPassword", "secretQuestion", "secretAnswer", "roles" ],
				propConfig: [
						password: [ type: "password" ],
						confirmPassword: [ type: "password" ],
						secretAnswer: [ type: "password" ]
				],
				fieldConfig: [
						roles: [ fieldFragment: "field/RoleCollection", hideRoles: disallowedRoles ]
				],
				extraFields: [
						[ hiddenInputName: "personId", value: person.id ]
				],
				submitLabel: "Save Changes",
				cancelLabel: "Cancel",
				onSuccessCallback: "ui.reloadPage();"
		]) %>

	<% } else if (!user) { %>

		<%= ui.includeFragment("chaiui", "widget/dialogForm", [
				buttonConfig: [
						label: "Create login",
						iconProvider: "chaiui",
						icon: "buttons/user_enable.png"
				],
				dialogConfig: [ heading: "New Login Account for ${ chaiui.formatPersonName(person) }", width: 90, height: 90 ],
				fragment: "account/userDetails",
				fragmentProvider: "chaiemr",
				action: "submit",
				prefix: "user",
				commandObject: form,
				properties: [ "username", "password", "confirmPassword", "secretQuestion", "secretAnswer", "roles" ],
				propConfig: [
						password: [ type: "password" ],
						confirmPassword: [ type: "password" ],
						secretAnswer: [ type: "password" ]
				],
				fieldConfig: [
						roles: [ fieldFragment: "field/RoleCollection", hideRoles: disallowedRoles ]
				],
				extraFields: [
						[ hiddenInputName: "personId", value: person.id ]
				],
				submitLabel: "Save Changes",
				cancelLabel: "Cancel",
				onSuccessCallback: "ui.reloadPage();"
		]) %>

	<% } %>
	</div>
</div>