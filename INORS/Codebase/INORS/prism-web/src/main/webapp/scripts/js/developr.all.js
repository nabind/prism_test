/**
 *
 * '||''|.                            '||
 *  ||   ||    ....  .... ...   ....   ||    ...   ... ...  ... ..
 *  ||    || .|...||  '|.  |  .|...||  ||  .|  '|.  ||'  ||  ||' ''
 *  ||    || ||        '|.|   ||       ||  ||   ||  ||    |  ||
 * .||...|'   '|...'    '|     '|...' .||.  '|..|'  ||...'  .||.
 *                                                  ||
 * --------------- By Display:inline ------------- '''' -----------
 *
 * Form inputs styling plugin
 *
 * Structural good practices from the article from Addy Osmani 'Essential jQuery plugin patterns'
 * @url http://coding.smashingmagazine.com/2011/10/11/essential-jquery-plugin-patterns/
 */

/*
 * The semi-colon before the function invocation is a safety
 * net against concatenated scripts and/or other plugins
 * that are not closed properly.
 */
;(function($, window, document, undefined)
{
	/*
	 * undefined is used here as the undefined global variable in ECMAScript 3 is mutable (i.e. it can
	 * be changed by someone else). undefined isn't really being passed in so we can ensure that its value is
	 * truly undefined. In ES5, undefined can no longer be modified.
	 */

	/*
	 * window and document are passed through as local variables rather than as globals, because this (slightly)
	 * quickens the resolution process and can be more efficiently minified.
	 */

	// Objects cache
	var win = $(window),
		doc = $(document);

	/**
	 * Convert switches, checkboxes and radios
	 * @param object options an object with any of the $.fn.styleCheckable.defaults options.
	 */
	$.fn.styleCheckable = function(options)
	{
		// Settings
		var globalSettings = $.extend({}, $.fn.styleCheckable.defaults, options);

		return this.each(function(i)
		{
			var element = $(this),
				settings = $.extend({}, globalSettings, element.data('checkable-options')),
				checked = this.checked ? ' checked' : '',
				disabled = this.disabled ? ' disabled' : '',
				replacement = element.data('replacement'),
				title = (this.title && this.title.length > 0) ? ' title="'+this.title+'"' : '',
				tabIndex = (this.tabIndex > 0) ? this.tabIndex : 0,
				isWatching;

			// If already set
			if (replacement)
			{
				return;
			}

			// Stop DOM watching
			isWatching = $.template.disableDOMWatch();

			// Create replacement
			if (element.hasClass('switch'))
			{
				replacement = $('<span class="'+this.className.replace(/validate\[.*\]/, '')+checked+disabled+' replacement"'+title+' tabindex="'+tabIndex+'">'+
									'<span class="switch-on"><span>'+(element.data('text-on') || settings.textOn)+'</span></span>'+
									'<span class="switch-off"><span>'+(element.data('text-off') || settings.textOff)+'</span></span>'+
									'<span class="switch-button"></span>'+
								'</span>');
			}
			else
			{
				replacement = $('<span class="'+this.className.replace(/validate\[.*\]/, '')+checked+disabled+' replacement"'+title+' tabindex="'+tabIndex+'">'+
									'<span class="check-knob"></span>'+
								'</span>');
			}

			// Prevent the element from being focusable by keyboard
			this.tabIndex = -1;

			// Insert
			replacement.insertAfter(element).data('replaced', element);

			// Store reference
			element.data('replacement', replacement);

			// Add clear function
			element.addClearFunction(_removeCheckableReplacement);

			// Move select inside replacement, and remove styling
			element.detach().appendTo(replacement).data('initial-classes', this.className);
			this.className = (this.className.indexOf('validate[') > -1) ? this.className.match(/validate\[.*\]/)[0] : '';

			// Re-enable DOM watching if required
			if (isWatching)
			{
				$.template.enableDOMWatch();
			}
		});
	};

	/*
	 * Options for styled switches, checkboxes and radios
	 */
	$.fn.styleCheckable.defaults = {
		/**
		 * Default text for ON value
		 * @var string
		 */
		textOn: 'ON',

		/**
		 * Default text for OFF value
		 * @var string
		 */
		textOff: 'OFF'
	};

	/**
	 * Convert selects
	 * @param object options an object with any of the $.fn.styleSelect.defaults options.
	 */
	$.fn.styleSelect = function(options)
	{
		// Settings
		var globalSettings = $.extend({}, $.fn.styleSelect.defaults, options);

		return this.each(function(i)
		{
			var element = $(this),
				settings = $.extend({}, globalSettings, element.data('select-options')),
				replacement = element.data('replacement'),
				hidden,
				extraWidth = 0,
				disabled = this.disabled ? ' disabled' : '',
				showAsMultiple = ((this.multiple || element.hasClass('multiple')) && !element.hasClass('multiple-as-single')),
				isSized = (element.attr('size') > 1),
				title = (this.title && this.title.length > 0) ? ' title="'+this.title+'"' : '',
				tabIndex = (this.tabIndex > 0) ? this.tabIndex : 0,
				width, widthString, select, dropDown, text, isWatching, values;

			// If already set
			if (replacement)
			{
				return;
			}

			// Stop DOM watching
			isWatching = $.template.disableDOMWatch();

			// Reveal hidden parents for correct width processing
			hidden = element.tempShow();

			// Element width
			if (element.is(':hidden'))
			{
				element.show();
			}
			width = element.width();

			// Restore hidden parents
			hidden.tempShowRevert();

			// If full width, no need to set width
			if (element.hasClass('full-width'))
			{
				widthString = '';
			}
			else
			{
				// Check if width has been set in the element styling
				if (this.style.width !== '' && this.style.width != 'auto')
				{
					extraWidth = showAsMultiple ? 0 : -26;
				}
				else
				{
					// Size adjustment
					if (this.multiple)
					{
						if (showAsMultiple)
						{
							extraWidth = element.hasClass('check-list') ? 36 : 8;
						}
						else
						{
							extraWidth = element.hasClass('check-list') ? 10 : 8;
						}
					}
					else if (showAsMultiple)
					{
						extraWidth = element.hasClass('check-list') ? 21 : 0;
					}

					// Space for scrollbar
					if (showAsMultiple && isSized)
					{
						extraWidth += $.fn.customScroll ? 6 : 20;
					}

					// Extra width for safari
					if (navigator.userAgent.match(/Safari/) && !navigator.userAgent.match(/Chrome/))
					{
						extraWidth += $.template.iPhone ? 6 : 23;
					}
				}

				// Final width string
				widthString = ' style="width:'+(width+extraWidth)+'px"';
			}

			// Create replacement
			if (showAsMultiple)
			{
				// Create
				select = $('<span class="'+this.className.replace(/validate\[.*\]/, '').replace(/(\s*)select(\s*)/, '$1selectMultiple$2')+disabled+' replacement"'+title+widthString+' tabindex="'+tabIndex+'">'+
								'<span class="drop-down"></span>'+
							'</span>')
				.insertAfter(element)
				.data('replaced', element);

				// Register
				element.data('replacement', select);

				// Load options
				_refreshSelectValues.call(select);

				// If the number of visible options is set
				if (isSized && !element.getStyleString().match(/height\s*:\s*[0-9]+/i))
				{
					// Set height
					dropDown = select.children('.drop-down');
					dropDown.height(element.hasClass('check-list') ? (this.size*37)-1 : this.size*26);

					// Enable scroll
					if ($.fn.customScroll)
					{
						dropDown.customScroll({
							padding: 4,
							showOnHover: false,
							usePadding: true
						});
					}
				}
			}
			else
			{
				// Create
				select = $('<span class="'+this.className.replace(/validate\[.*\]/, '')+disabled+' replacement"'+title+widthString+' tabindex="'+tabIndex+'">'+
								'<span class="select-value"></span>'+
								'<span class="select-arrow">'+($.template.ie7 ? '<span class="select-arrow-before"></span><span class="select-arrow-after"></span>' : '')+'</span>'+
								'<span class="drop-down"></span>'+
							'</span>')
				.insertAfter(element)
				.data('replaced', element)
				.on('select-prepare-open', _refreshSelectValues); // Load at first opening to reduce startup load

				// Gather selected values texts
				values = [];
				element.find(':selected').each(function(i)
				{
					values.push($(this).text());
				});

				// Update displayed value
				if (this.multiple)
				{
					switch (values.length)
					{
						case 1:
							_updateSelectValueText(select, values, element.data('single-value-text'), settings.singleValueText);
							break;

						case this.options.length:
							_updateSelectValueText(select, values, element.data('all-values-text'), settings.allValuesText);
							break;

						default:
							_updateSelectValueText(select, values, element.data('multiple-values-text'), settings.multipleValuesText);
							break;
					}
				}
				else
				{
					select.children('.select-value').html((values.length > 0) ? values.join(', ') : '&nbsp;');
				}

				// Register
				element.data('replacement', select);
			}

			// Custom event to refresh values list
			element.on('update-select-list', function(event)
			{
				_refreshSelectValues.apply(select[0]);
			});

			// Prevent the element from being focusable by keyboard
			this.tabIndex = -1;

			// Move select inside replacement, and remove styling
			element.detach().appendTo(select).data('initial-classes', this.className);
			this.className = (this.className.indexOf('validate[') > -1) ? this.className.match(/validate\[.*\]/)[0] : '';

			// Add clear function
			element.addClearFunction(_removeSelectReplacement);

			// Store settings
			select.data('select-settings', settings);

			/*
			 * To avoid triggering the default select UI, the select is hidden if:
			 * - it is displayed as multiple (even if simple) OR
			 * - it is multiple (no overlaying UI in most OS) OR
			 * - The setting styledList is on AND
			 *      - This is not a touch device OR
			 *      - This is a touch device AND the setting styledOnTouch is:
			 *          - true OR
			 *          - null and the select has the class 'check-list'
			 *
			 * Ew. Now I need to get another brain.
			 */
			if (showAsMultiple ||
				this.multiple ||
				(settings.styledList &&
					(!$.template.touchOs ||
					($.template.touchOs &&
						(settings.styledOnTouch === true ||
						(settings.styledOnTouch === null && select.hasClass('check-list')))))))
			{
				element.hide();
			}

			// Re-enable DOM watching if required
			if (isWatching)
			{
				$.template.enableDOMWatch();
			}
		});
	};

	/*
	 * Options for styled selects
	 */
	$.fn.styleSelect.defaults = {
		/**
		 * False to use system's drop-down UI, true to use style's drop-downs
		 * @var boolean
		 */
		styledList: true,

		/**
		 * For touch devices: false to use system's drop-down UI, true to use style's drop-downs, or null to guess (true for check-list style, false for others)
		 * Note: only works if styledList is true
		 * @var boolean|null
		 */
		styledOnTouch: null,

		/**
		 * When focused, should the arrow down key open the drop-down or just scroll values?
		 * @var boolean
		 */
		openOnKeyDown: true,

		/**
		 * Text for multiple select with no value selected
		 * @var string
		 */
		noValueText: '',

		/**
		 * Text for multiple select with one value selected, or false to just display the selected value
		 * @var string|boolean
		 */
		singleValueText: false,

		/**
		 * Text for multiple select with multiple values selected, or false to just display the selected list
		 * Tip: use %d as a placeholder for the number of values
		 * @var string|boolean
		 */
		multipleValuesText: '%d selected',

		/**
		 * Text for multiple select with all values selected, or false to just display the selected list
		 * Tip: use %d as a placeholder for the number of values
		 * @var string|boolean
		 */
		allValuesText: 'ALL',

		/**
		 * Enable search field when open - use null to automatically use when list has more than searchIfMoreThan elements
		 * @var boolean|null
		 */
		searchField: null,

		/**
		 * Minimum number of elements to trigger a search field, if searchField is null
		 * @var int
		 */
		searchIfMoreThan: 40,

		/**
		 * Helper text for seach field
		 * @var string
		 */
		searchText: 'Search'
	};

	/**
	 * Convert file inputs
	 * @param object options an object with any of the $.fn.styleFile.defaults options.
	 */
	$.fn.styleFile = function(options)
	{
		// Settings
		var globalSettings = $.extend({}, $.fn.styleFile.defaults, options);

		return this.each(function(i)
		{
			var element = $(this).addClass('file'),
				settings = $.extend({}, globalSettings, element.data('file-options')),
				blackInput = (element.hasClass('black-input') || element.closest('.black-inputs').length > 0) ? ' anthracite-gradient' : '',
				multiple = !!this.multiple,
				disabled = this.disabled ? ' disabled' : '',
				isWatching;

			// If already set
			if (element.parent().hasClass('file'))
			{
				return;
			}

			// Stop DOM watching
			isWatching = $.template.disableDOMWatch();

			// Create styling
			styling = $('<span class="input '+this.className.replace(/validate\[.*\]/, '')+disabled+'">'+
							'<span class="file-text">'+element.val()+'</span>'+
							'<span class="button compact'+blackInput+'">'+(multiple ? settings.textMultiple : settings.textSingle)+'</span>'+
						'</span>');

			// Insert
			styling.insertAfter(element);

			// Add clear function
			element.addClearFunction(_removeInputStyling);

			// Move select inside styling
			element.detach().appendTo(styling);

			// Re-enable DOM watching if required
			if (isWatching)
			{
				$.template.enableDOMWatch();
			}
		});
	};

	/*
	 * Options for styled switches, checkboxes and radios
	 */
	$.fn.styleFile.defaults = {
		/**
		 * Button text - single file
		 * @var string
		 */
		textSingle: 'Select file',

		/**
		 * Button text - multiple files
		 * @var string
		 */
		textMultiple: 'Select files'
	};

	/**
	 * Set the value of a number input
	 * @param number value the value to set
	 */
	$.fn.setNumber = function(value)
	{
		return this.each(function(i)
		{
			var input;

			// Detect input
			if (this.nodeName.toLowerCase() === 'input')
			{
				input = $(this);
			}
			else
			{
				input = $(this).children('input:first');
				if (input.length === 0)
				{
					return;
				}
			}

			// Set value
			input.val(_formatNumberValue(value, _getNumberOptions(input)));
		});
	};

	/**
	 * Increment/decrement the value of a number input
	 * @param boolean up true if the value should be incremented, false for decremented
	 * @param boolean shift whether to use shiftIncrement or not (optional, default: false)
	 */
	$.fn.incrementNumber = function(up, shift)
	{
		return this.each(function(i)
		{
			var input, options, value;

			// Detect input
			if (this.nodeName.toLowerCase() === 'input')
			{
				input = $(this);
			}
			else
			{
				input = $(this).children('input:first');
				if (input.length === 0)
				{
					return;
				}
			}

			// Options
			options = _getNumberOptions(input);

			// Remove format
			value = _unformatNumberValue(input.val(), options);

			// Check if numeric
			if (isNaN(value))
			{
				value = 0;
			}

			// Increment value
			value += up ? (shift ? options.shiftIncrement : options.increment) : (shift ? -options.shiftIncrement : -options.increment);

			// Set value
			input.val(_formatNumberValue(value, options));
		});
	};

	/**
	 * Helper function: load and format number input options
	 * @param jQuery input the target input
	 * @return object the options object
	 */
	function _getNumberOptions(input)
	{
		var options = input.data('number-options'),
			temp;

		// If not set yet or not formatted
		if (!options || !options.formatted)
		{
			// Extend
			options = $.extend({}, $.fn.setNumber.defaults, options);

			// Validate
			if (typeof options.min !== 'number')
			{
				options.min = null;
			}
			if (typeof options.max !== 'number')
			{
				options.max = null;
			}
			if (options.min !== null && options.max !== null)
			{
				if (options.min > options.max)
				{
					temp = options.max;
					options.max = options.min;
					options.min = temp;
				}
			}
			if (!options.precision)
			{
				options.precision = 1;
			}

			// Set as ready
			options.formatted = true;
			input.data('number-options', options);
		}

		return options;
	}

	/**
	 * Helper function: remove user format of a number value according to options
	 * @param value the value
	 * @param object options the validated options
	 * @return number the valid value
	 */
	function _unformatNumberValue(value, options)
	{
		if (typeof value !== 'number')
		{
			if (options.thousandsSep.length)
			{
				value = value.replace(options.thousandsSep, '');
			}
			if (options.decimalPoint !== '.')
			{
				value = value.replace(options.decimalPoint, '.');
			}
			value = parseFloat(value);
			if (isNaN(value))
			{
				value = 0;
			}
		}

		return value;
	}

	/**
	 * Helper function: format a number value according to options
	 * @param value the value
	 * @param object options the validated options
	 * @return number|string the valid value
	 */
	function _formatNumberValue(value, options)
	{
		var parts;

		// Remove format
		value = _unformatNumberValue(value, options);

		// Round value
		value = Math.round(value/options.precision)*options.precision;

		// Check min/max
		if (options.min !== null)
		{
			value = Math.max(value, options.min);
		}
		if (options.max !== null)
		{
			value = Math.min(value, options.max);
		}

		// Format value
		parts = value.toString().split('.');

		// Thousands separator
		if (options.thousandsSep.length && parts[0].length > 3)
		{
			parts[0] = parts[0].replace(/\B(?=(?:\d{3})+(?!\d))/g, options.thousandsSep);
		}

		return parts.join(options.decimalPoint);
	}

	/*
	 * Options for number inputs
	 */
	$.fn.setNumber.defaults = {
		/**
		 * Minimum value (null for none)
		 * @var number|null
		 */
		min: null,

		/**
		 * Maximum value (null for none)
		 * @var number|null
		 */
		max: null,

		/**
		 * Increment of up/down arrows and keys
		 * @var number
		 */
		increment: 1,

		/**
		 * Increment of up/down arrows and keys when holding shift key
		 * @var number
		 */
		shiftIncrement: 10,

		/**
		 * Precision of the value: the user input will be rounded using it.
		 * For instance, use 1 for rounded nombers, 0.25 to user quarter increments...
		 * @var number
		 */
		precision: 1,

		/**
		 * Character used for decimal point
		 * @var string
		 */
		decimalPoint: '.',

		/**
		 * Character used for thousands separator
		 * @var string
		 */
		thousandsSep: ''
	};

	/**
	 * Helper function to check if an element is an input/select/textarea/button and may be disabled
	 * @param jQuery element the element to check
	 * @return boolean true if the element may be disabled, else false
	 */
	function mayBeDisabled(element)
	{
		var nodeName = element[0].nodeName.toLowerCase();
		return (nodeName === 'input' || nodeName === 'select' || nodeName === 'textarea' || nodeName === 'button');
	}

	/**
	 * Enable a form input, and update the styled UI
	 */
	$.fn.enableInput = function()
	{
		return this.each(function(i)
		{
			var element = $(this),
				replacement, replaced;

			// Inputs
			if (mayBeDisabled(element))
			{
				// Enable
				element.prop('disabled', false);

				// Style replacement
				replacement = element.data('replacement');
				if (replacement)
				{
					replacement.removeClass('disabled');
				}
			}
			// Replacements
			else
			{
				// Look for input
				replaced = element.data('replaced');
				if (replaced && mayBeDisabled(replaced))
				{
					// Enable input
					replaced.prop('disabled', false);

					// Style replacement
					element.removeClass('disabled');
				}
			}
		});
	};

	/**
	 * Disable a form input, and update the styled UI
	 */
	$.fn.disableInput = function()
	{
		return this.each(function(i)
		{
			var element = $(this),
				replacement, replaced;

			// Inputs
			if (mayBeDisabled(element))
			{
				// Enable
				element.prop('disabled', true);

				// Style replacement
				replacement = element.data('replacement');
				if (replacement)
				{
					replacement.addClass('disabled');
				}
			}
			// Replacements
			else
			{
				// Look for input
				replaced = element.data('replaced');
				if (replaced && mayBeDisabled(replaced))
				{
					// Enable input
					replaced.prop('disabled', true);

					// Style replacement
					element.addClass('disabled');
				}
			}
		});
	};

	// Add to template setup function
	$.template.addSetupFunction(function(self, children)
	{
		var elements = this;

		// Switches, checkboxes and radios
		elements.findIn(self, children, 'input.switch, input.checkbox, input.radio').each(function(i)
		{
			// Style element
			$(this).styleCheckable();

			// If in the root target, add to selection
			if (self && elements.is(this))
			{
				elements = elements.add(this);
			}
		});

		// Checkables in buttons
		elements.findIn(self, children, 'label.button').children(':radio, :checkbox').each(function(i)
		{
			// Style element
			if (this.checked)
			{
				$(this).parent().addClass('active');
			}
		});

		// File inputs
		elements.findIn(self, children, '.file').filter('input[type="file"]').styleFile();

		// Placeholder polyfill
		if (!Modernizr.input.placeholder)
		{
			elements.findIn(self, children, 'input[placeholder][type!="password"]').each(function(i)
			{
				var input = $(this),
					placeholder = input.attr('placeholder');

				// Mark and add data for validation plugin
				input.addClass('placeholder').attr('data-validation-placeholder', placeholder);

				// Fill if empty
				if ($.trim(input.val()) === '')
				{
					input.val(placeholder);
				}
			});
		}

		// Selects
		elements.findIn(self, children, 'select.select').each(function(i)
		{
			// Style element
			$(this).styleSelect();

			// If in the root target, add to selection
			if (self && elements.is(this))
			{
				elements = elements.add(this);
			}
		});

		return elements;
	});

	/********************************************************/
	/*                   Helper functions                   */
	/********************************************************/

	/**
	 * Open a select drop-down list
	 *
	 * @param jQuery select the replacement select
	 * @param boolean onHover whether the select was open on hover or not (optional, default: none)
	 * @param event the opening event (optional)
	 * @return void
	 */
	function _openSelect(select, onHover, event)
	{
		var replaced = select.data('replaced'),
			settings = select.data('select-settings') || {},
			list = select.children('.drop-down'),
			formAttr, form, placeholder,
			addedClasses = [], inheritParent,
			scrollParents, hasFocus,
			position, listOffset,
			winHeight, listHeight, optionHeight,
			listExtra, availableHeight,
			fixedSize = false,
			search = false, searchSpan, searchField,
			date = new Date(), time = date.getTime(),
			isWatching, updateList, onBlur;

		// Prevent event default
		if (event)
		{
			event.preventDefault();
		}

		// Do not handle if disabled
		if (select.closest('.disabled').length > 0 || (replaced && replaced.is(':disabled')))
		{
			return;
		}

		// Do not handle if the OS UI should be used
		if (replaced && !replaced.is(':hidden'))
		{
			return;
		}

		// Parent form
		if (replaced)
		{
			formAttr = replaced.attr('form');
			form = (!formAttr || formAttr === '') ? replaced.closest('form') : $('#'+formAttr);
		}

		// If not open yet
		if (!select.hasClass('open') && list.length > 0)
		{
			// List of scrolling parents
			scrollParents = select.parents('.custom-scroll');

			// Add class if the select is in a top-level element
			if (select.closest('.modal, .notification, .tooltip').length > 0)
			{
				select.addClass('over');
			}

			// Stop DOM watching
			isWatching = $.template.disableDOMWatch();

			// Position
			selectOffset = select.offset();

			// Check if has focus
			hasFocus = select.is(':focus');

			// Placeholder
			placeholder = $('<span class="'+select[0].className+'" style="'+
								'width: '+select.width()+'px !important; '+
								'-webkit-box-shadow: none !important; '+
								'-moz-box-shadow: none !important; '+
								'box-shadow: none !important;'+
							'"></span>').insertBefore(select).append(select.children('.select-value').clone());

			// Size for fluid elements
			if (select.hasClass('full-width'))
			{
				select.css({ width: select.width()+'px' });
				fixedSize = true;
			}

			/*
			 * Inherited classes check
			 */

			// Glossy
			if (!select.is('.glossy'))
			{
				inheritParent = select.closest('.glossy');
				if (inheritParent.length > 0)
				{
					addedClasses.push('glossy');
				}
			}

			// Size
			if (!select.is('.compact'))
			{
				inheritParent = select.parent('.compact');
				if (inheritParent.length > 0)
				{
					addedClasses.push('compact');
				}
			}

			// If any extra class found
			if (addedClasses.length > 0)
			{
				select.addClass(addedClasses.join(' '));
			}

			// Detach and put on top of everything, then track placeholder's position
			select.detach().appendTo(document.body).trackElement(placeholder);

			// Re-enable DOM watching if required
			if (isWatching)
			{
				$.template.enableDOMWatch();
			}

			// Restore focus if required
			if (hasFocus)
			{
				select.focus();
			}

			// Prepare and open
			select.removeClass('reversed')
				.trigger('select-prepare-open')
				.addClass('open')
				.trigger('select-open')
				.on('click', _preventSelectClick);

			/*
			 * Search field
			 */

			// If search field should be used
			if (!select.hasClass('auto-open') && (settings.searchField === true || (settings.searchField === null && list.children('a, span').length >= settings.searchIfMoreThan)))
			{
				// Create elements
				search = $('<span class="select-search"></span>').appendTo(select);
				searchSpan = $('<span>'+settings.searchText+'</span>').appendTo(search);
				searchField = $('<input type="text" value="">').appendTo(search);

				// Behavior
				search.on('keydown click touchend', function(event)
				{
					event.stopPropagation();
				});
				searchField.focus(function()
				{
					select.addClass('focus');

				}).blur(function()
				{
					select.removeClass('focus');

				}).keyup(function(event)
				{
					var text = $.trim(searchField.val()),
						searchRegex;

					// If search is empty
					if (text.length === 0)
					{
						list.children().show();
						searchSpan.fadeIn();
						return;
					}

					// Hide placeholder
					searchSpan.hide();

					// Regular expression
					searchRegex = new RegExp($.trim(searchField.val()).toLowerCase(), 'g');

					// Loop through values to find a match
					list.children('a, span').each(function(i)
					{
						var option = $(this);

						// If matches
						if ($.trim(option.text().toLowerCase()).match(searchRegex))
						{
							option.show();
						}
						else
						{
							option.hide();
						}
					});
				});
			}

			/*
			 * Set select list position according to available screen space
			 */

			// Add scroll
			if ($.fn.customScroll)
			{
				if (!list.hasCustomScroll())
				{
					list.customScroll({
						padding: 4,
						showOnHover: false,
						usePadding: true
					});
				}
			}

			// Get heights
			listOffset = list.removeClass('reversed').position().top;
			listHeight = list.outerHeight();
			listExtra = listHeight-list.height();

			// Function to refresh position on resize/scroll
			updateList = function()
			{
				var scrollPos;

				// Refresh size
				listHeight = list.css('max-height', '').outerHeight();

				// Select vertical position
				position = select.offset().top-win.scrollTop();

				// Viewport height
				winHeight = win.height();

				// If too long to fit
				if (position+listOffset+listHeight > winHeight)
				{
					// Check if it fits on top
					if (position-listOffset-listHeight > 0)
					{
						// Display on top
						select.addClass('reversed');
					}
					/*
					 * Now we know that the list can't be displayed full size, so we truncate it.
					 * If the select is above 60% of screen height, it will show under, otherwise on top
					 */
					else
					{
						if (position > winHeight*0.6)
						{
							// Display on top
							select.addClass('reversed');
							availableHeight = position;
						}
						else
						{
							// Display under
							select.removeClass('reversed');
							availableHeight = winHeight-position-listOffset;
						}

						// Remove list padding/borders from available size
						availableHeight -= listExtra;

						// Set max-height to use available space
						list.css({
							maxHeight: (availableHeight-10)+'px'
						});

						// Try to restore scroll position
						scrollPos = select.data('scrollPosition');
						if (scrollPos)
						{
							list[0].scrollTop = scrollPos;
						}
					}
				}
				else
				{
					// Clear changes
					select.removeClass('reversed');
				}

				// Clear data
				select.removeData('scrollPosition');

				// Update scroll
				if ($.fn.customScroll)
				{
					list.refreshCustomScroll();
				}
			};

			// Function to handle focus loss
			onBlur = function(event)
			{
				// Remove events
				win.off('resize', updateList);
				doc.off('scroll', onBlur);
				if (form)
				{
					form.off('submit', onBlur);
				}
				scrollParents.off('scroll', onBlur);
				if (onHover && !$.template.touchOs)
				{
					select.off('mouseleave', onBlur);
				}
				else
				{
					doc.off('touchend click', onBlur);
				}

				// Clear data
				select.removeData('selectCloseFunction');

				// Check if has focus
				var hasFocus = select.is(':focus');

				// Remove search field
				if (search)
				{
					if (searchField.is(':focus'))
					{
						hasFocus = true;
					}
					search.remove();
					list.children().show();
				}

				// Size for fluid elements
				if (fixedSize)
				{
					select.css({ width: '' });
				}

				// Inherited classes
				if (addedClasses.length > 0)
				{
					select.removeClass(addedClasses.join(' '));
				}

				// Store scroll position for later re-opening
				select.data('scrollPosition', list[0].scrollTop);

				// Stop DOM watching
				isWatching = $.template.disableDOMWatch();

				// Put element back in place
				select.stopTracking(true)
					.off('click', _preventSelectClick)
					.removeClass('over')
					.detach()
					.insertAfter(placeholder)
					.trigger('select-prepare-close')
					.removeClass('open')
					.trigger('select-close');
				placeholder.remove();

				// Re-enable DOM watching if required
				if (isWatching)
				{
					$.template.enableDOMWatch();
				}

				// Restore focus if required
				if (hasFocus)
				{
					select.focus();
				}
			};

			// Store for external calls
			select.data('selectCloseFunction', onBlur);

			// First call and binding
			updateList();
			win.on('resize', updateList);
			doc.on('scroll', onBlur);
			if (form)
			{
				form.on('submit', onBlur);
			}
			scrollParents.on('scroll', onBlur);
			if (onHover && !$.template.touchOs)
			{
				select.on('mouseleave', onBlur);
			}
			else
			{
				doc.on('click', onBlur);
			}
		}
	}

	/**
	 * Prevent the click event from bubbling when open
	 *
	 * @return void
	 */
	function _preventSelectClick(event)
	{
		event.preventDefault();
	}

	/**
	 * Refresh select values
	 *
	 * @return void
	 */
	function _refreshSelectValues()
	{
		var select = $(this),
			list = select.children('.drop-down'),
			replaced = select.data('replaced'),
			checkList = select.hasClass('check-list') ? '<span class="check"></span>' : '',
			isWatching;

		// If valid
		if (list.length > 0 && replaced)
		{
			// Disable DOM watching for better performance
			isWatching = $.template.disableDOMWatch();

			list.empty();
			replaced.find('option, optgroup').each(function(i)
			{
				var classes = [],
					option = (this.nodeName.toLowerCase() === 'option'),
					node = option ? 'span' : 'strong',
					text = option ? $(this).text() : this.label;

				// Mode
				if (option)
				{
					// State
					if (this.selected)
					{
						classes.push('selected');
					}

					// If in an optgroup
					if (this.parentNode.nodeName.toLowerCase() === 'optgroup')
					{
						classes.push('in-group');
					}

					// If disabled
					if (this.disabled)
					{
						classes.push('disabled');
					}
				}

				// Empty text
				if (text.length === 0)
				{
					text = '&nbsp;';
				}

				$('<'+node+((classes.length > 0) ? ' class="'+classes.join(' ')+'"' : '')+'>'+checkList+text+'</'+node+'>')
					.appendTo(list)
					.data('select-value', this);
			});
			list.children('span').not('.disabled').on('touchend click', _clickSelectValue);

			// Re-enable DOM watching if required
			if (isWatching)
			{
				$.template.enableDOMWatch();
			}

			// Remove binding
			select.off('select-prepare-open', _refreshSelectValues);
		}
	}

	/**
	 * Select a list value
	 *
	 * @param object event
	 * @return void
	 */
	function _clickSelectValue(event)
	{
		// Check if valid touch-click event
		if (!$.template.processTouchClick(this, event))
		{
			event.stopPropagation();
			return;
		}

		var option = $(this),
			list = option.parent(),
			select = list.parent(),
			replaced = select.data('replaced'),
			replacedOption = option.data('select-value'),
			multiple = replaced[0].multiple,
			selected, value;

		// Detect touch scrolling
		if (list.data('touch-scrolling'))
		{
			return;
		}

		// If valid
		if (replaced && replacedOption)
		{
			// If multiple selection and holding ctrl/cmd
			if (multiple && ($.template.touchOs || event.ctrlKey || event.metaKey || select.hasClass('easy-multiple-selection')))
			{
				// Current option state
				selected = option.hasClass('selected');

				// Multiple selects require a last one selected option, except if marked
				if (!select.hasClass('allow-empty'))
				{
					// Only change if the option wasn't selected, or if there is at least one other selected option
					if (!selected || (selected && (value = replaced.val()) && value.length > 1))
					{
						// Update select
						replacedOption.selected = !selected;
						replaced.trigger('change');
					}
				}
				else
				{
					// Default behavior
					replacedOption.selected = !selected;
					replaced.trigger('change');
				}

				// Stop propagation to allow multiple selection
				if (event.type === 'touchend' || !select.hasClass('selectMultiple'))
				{
					event.stopPropagation();
				}
			}
			// Standard selection mode
			else
			{
				// Get current value
				value = replaced.val();
				if (multiple && (value === null || value === undefined))
				{
					value = [];
				}

				// Compare depending on mode
				if ((multiple && (value.length !== 1 || value[0] !== replacedOption.value)) || (!multiple && value !== replacedOption.value))
				{
					// Update select
					replaced.val(replacedOption.value).trigger('change');
				}
			}
		}
	}

	/**
	 * Set the select replacement text according to options
	 *
	 * @param jQuery select the replacement select
	 * @param array values the list of selected values text
	 * @param string|boolean dataText template specified in the element's data, if any
	 * @param string|boolean defaultText default value
	 * @return void
	 */
	function _updateSelectValueText(select, values, dataText, defaultText)
	{
		// If no user value, use default
		if (!dataText)
		{
			dataText = defaultText;
		}

		// Must not be empty to preserve vertical-align
		if (typeof dataText === 'string' && dataText.length === 0)
		{
			dataText = '&nbsp;';
		}

		// Check format
		if (typeof dataText === 'boolean')
		{
			select.children('.select-value').removeClass('alt').html((values.length > 0) ? values.join(', ') : '&nbsp;');
		}
		else
		{
			select.children('.select-value').addClass('alt').html(dataText.replace('%d', values.length));
		}
	}

	/**
	 * Get a select selected value index
	 *
	 * @param jQuery select the select selection
	 * @return int|boolean, the selected index, or -1 if none, or false if several values are selected
	 */
	function _getSelectedIndex(select)
	{
		// Mode
		if (select[0].multiple)
		{
			// Multiple select values
			val = select.val();

			// If several values
			if (val && val.length > 1)
			{
				selectedIndex = false;
			}
			else
			{
				selectedIndex = select[0].selectedIndex;
			}
		}
		else
		{
			selectedIndex = select[0].selectedIndex;
		}

		// Detect if undefined
		if (selectedIndex === null || selectedIndex === undefined)
		{
			selectedIndex = -1;
		}

		return selectedIndex;
	}

	/**
	 * Clean delete of a radio/checkbox replacement
	 *
	 * @return void
	 */
	function _removeCheckableReplacement()
	{
		var element = $(this),
			replacement = element.data('replacement'),
			blurFunc;

		// If not replaced
		if (!replacement)
		{
			return;
		}

		// If focused
		blurFunc = replacement.data('checkableBlurFunction');
		if (blurFunc)
		{
			blurFunc();
		}

		// Tabindex
		this.tabIndex = select[0].tabIndex;

		// Remove select from replacement and restore classes
		element.detach().insertBefore(replacement).css('display', '');
		this.className = element.data('initial-classes');
		element.removeData('initial-classes');

		// Remove references
		element.removeData('replacement');

		// Delete replacement
		replacement.remove();
	}

	/**
	 * Clean delete of a select replacement
	 *
	 * @return void
	 */
	function _removeSelectReplacement()
	{
		var element = $(this),
			select = element.data('replacement'),
			closeFunc, blurFunc;

		// If not replaced
		if (!select)
		{
			return;
		}

		// If open
		closeFunc = select.data('selectCloseFunction');
		if (closeFunc)
		{
			closeFunc();
		}

		// If focused
		blurFunc = select.data('selectBlurFunction');
		if (blurFunc)
		{
			blurFunc();
		}

		// Tabindex
		this.tabIndex = select[0].tabIndex;

		// Remove select from replacement and restore classes
		element.detach().insertBefore(select).css('display', '');
		this.className = element.data('initial-classes');
		element.removeData('initial-classes');

		// Remove references
		element.removeData('replacement');

		// Stop scrolling
		if ($.fn.customScroll)
		{
			select.children('.drop-down').removeCustomScroll();
		}

		// Delete select
		select.remove();
	}

	/**
	 * Clean delete of a file input replacement
	 *
	 * @return void
	 */
	function _removeInputStyling()
	{
		var element = $(this),
			parent = element.parent();

		// If not replaced
		if (!parent.hasClass('file'))
		{
			return;
		}

		// Remove input from styling
		element.detach().insertBefore(parent);

		// Delete styling
		parent.remove();
	}

	/********************************************************/
	/*        Event delegation for template elements        */
	/********************************************************/

	/*
	 * Event delegation is used to handle most of the template setup, as it does also apply to dynamically added elements
	 * @see http://api.jquery.com/on/
	 */

	doc.on('click', 'label', function(event)
	{
		var label = $(this),
			element = $('#'+this.htmlFor),
			replacement;

		// If no input, exit
		if (element.length === 0)
		{
			return;
		}

		// Replacement
		replacement = element.data('replacement');

		// IE7/8 only triggers 'change' on blur and does not handle change on 'click' for hidden elements, so we need to use a workaround
		if ($.template.ie7 || $.template.ie8)
		{
			// If checkbox/radio
			if (element.is(':checkbox, :radio'))
			{
				// If replaced
				if (replacement)
				{
					// Trigger event
					replacement.trigger('click');
					return;
				}

				// If checkable is included in label
				if (label.hasClass('button') && element.closest('label').is(label))
				{
					// Do not handle if disabled
					if (element.closest('.disabled').length > 0 || element.is(':disabled'))
					{
						return;
					}

					// Check if state can be changed
					if (element.is(':checkbox') || !element.prop('checked'))
					{
						element.prop('checked', !element.prop('checked')).trigger('change');
					}
				}

				return;
			}
		}

		// If hidden select
		if (element.is('select'))
		{
			// Only process if hidden
			if (replacement && element.is(':hidden'))
			{
				replacement.focus();
			}
		}
	});

	// Change radio/checkboxes
	doc.on('click', 'span.switch, span.radio, span.checkbox', function(event)
	{
		var element = $(this),
			replaced = element.data('replaced');

		// If not valid, exit
		if (replaced.length === 0)
		{
			return;
		}

		// Only process if not clicking in the inner checkable
		if (event.target === replaced[0])
		{
			return;
		}

		// Do not handle if disabled
		if (element.closest('.disabled').length > 0 || replaced.is(':disabled'))
		{
			return;
		}

		// If dragged too recently
		if (element.data('switch-dragged'))
		{
			return;
		}

		// Check if state can be changed
		if (replaced.is(':checkbox') || !replaced.prop('checked'))
		{
			replaced.prop('checked', !replaced.prop('checked')).trigger('change');
		}
	});

	// Drag switches
	doc.on('mousedown touchstart', 'span.switch', function(event)
	{
			// Parent switch
		var switchEl = $(this),
			replaced = switchEl.data('replaced'),
			reversed = (switchEl.closest('.reversed-switches').length > 0),

			// Button
			button = switchEl.children('.switch-button'),

			// Is it a mini/tiny switch
			mini = switchEl.hasClass('mini'),
			tiny = switchEl.hasClass('tiny'),

			// Size adjustments
			buttonOverflow = tiny ? 2 : 0,
			valuesOverflow = ((mini || tiny) ? 7 : 4)+(2*buttonOverflow),
			marginIE7 = ($.template.ie7 && !mini && !tiny) ? 4 : 0,

			// Original button position
			initialPosition = button.position().left,

			// Inner elements
			onEl = switchEl.children('.switch-on'),
			onSpan = onEl.children(),
			offEl = switchEl.children('.switch-off'),
			offSpan = offEl.children(),

			// Available space
			switchWidth = switchEl.width(),
			buttonWidth = button.outerWidth(true),
			availableSpace = switchWidth-buttonWidth+(2*buttonOverflow),

			// Type of event
			touchEvent = (event.type === 'touchstart'),

			// Event start position
			offsetHolder = touchEvent ? event.originalEvent.touches[0] : event,
			mouseX = offsetHolder.pageX,

			// Work vars
			ieSelectStart, dragged = false, value;

		// If not valid, exit
		if (replaced.length === 0)
		{
			return;
		}

		// Do not handle if disabled
		if (switchEl.closest('.disabled').length || replaced.is(':disabled'))
		{
			return;
		}

		// Stop text selection
		event.preventDefault();
		ieSelectStart = document.onselectstart;
		document.onselectstart = function()
		{
			return false;
		};

		// Add class to prevent animation
		switchEl.addClass('dragging');

		// Watch mouse/finger move
		function watchMouse(event)
		{
			var offsetHolder = touchEvent ? event.originalEvent.touches[0] : event,
				position = Math.max(0, Math.min(availableSpace, initialPosition+(offsetHolder.pageX-mouseX)));

			// Actual value
			value = (position > availableSpace/2) ? !reversed : reversed;

			// Move inner elements
			if (reversed)
			{
				button.css('right', (availableSpace-position-buttonOverflow)+'px');
				offEl.css('right', (switchWidth-position-valuesOverflow)+'px');
				offSpan.css('margin-left', -(availableSpace-position+marginIE7)+'px');
				onEl.css('left', (buttonWidth+position-valuesOverflow)+'px');
			}
			else
			{
				button.css('left', (position-buttonOverflow)+'px');
				onEl.css('right', (switchWidth-position-valuesOverflow)+'px');
				onSpan.css('margin-left', -(availableSpace-position+marginIE7)+'px');
				offEl.css('left', (buttonWidth+position-valuesOverflow)+'px');
			}

			// Drag is effective
			dragged = true;
		}
		doc.on(touchEvent ? 'touchmove' : 'mousemove', watchMouse);

		// Watch for mouseup/touchend
		function endDrag()
		{
			doc.off(touchEvent ? 'touchmove' : 'mousemove', watchMouse);
			doc.off(touchEvent ? 'touchend' : 'mouseup', endDrag);

			// Remove class preventing animation
			switchEl.removeClass('dragging');

			// Reset positions
			if (reversed)
			{
				button.css('right', '');
				offEl.css('right', '');
				offSpan.css('margin-left', '');
				onEl.css('left', '');
			}
			else
			{
				button.css('left', '');
				onEl.css('right', '');
				onSpan.css('margin-left', '');
				offEl.css('left', '');
			}

			// Re-enable text selection
			document.onselectstart = ieSelectStart ? ieSelectStart : null;

			// If dragged, update value
			if (dragged)
			{
				// Set new value
				if (replaced.prop('checked') != value)
				{
					replaced.prop('checked', value).change();
				}

				// Prevent change on upcoming click event
				switchEl.data('switch-dragged', true);
				setTimeout(function()
				{
					switchEl.removeData('switch-dragged');

				}, 40);
			}
			else if (touchEvent)
			{
				// Click event is not trigerred for touch devices when touch events were handled
				switchEl.click();
			}
		}
		doc.on(touchEvent ? 'touchend' : 'mouseup', endDrag);
	});

	// Radios and checkboxes changes
	doc.on('change', ':radio, :checkbox', function(event)
	{
		var element = $(this),
			replacement = element.data('replacement'),
			checked = this.checked;

		// Update visual style
		if (replacement)
		{
			// Update style
			replacement[checked ? 'addClass' : 'removeClass']('checked');
		}
		// Button labels
		else if (element.parent().is('label.button'))
		{
			element.parent()[checked ? 'addClass' : 'removeClass']('active');
		}

		// If radio, refresh others without triggering 'change'
		if (this.type === 'radio')
		{
			$('input[name="'+this.name+'"]:radio').not(this).each(function(i)
			{
				var input = $(this),
					replacement = input.data('replacement');

				// Switch
				if (replacement)
				{
					replacement[checked ? 'removeClass' : 'addClass']('checked');
				}
				// Button labels
				else if (input.parent().is('label.button'))
				{
					input.parent()[checked ? 'removeClass' : 'addClass']('active');
				}

				// Trigger special event
				input.trigger('silent-change');
			});
		}
	});

	// Switches, radios and checkboxes focus
	doc.on('focus', 'span.switch, span.radio, span.checkbox', function(event)
	{
		var element = $(this),
			replaced = element.data('replaced'),
			handleKeysEvents = false;

		// If not valid, exit
		if (replaced.length === 0)
		{
			return;
		}

		// Do not handle if disabled
		if (element.closest('.disabled').length > 0 || replaced.is(':disabled'))
		{
			event.preventDefault();
			return;
		}

		// IE7-8 focus handle is different from modern browsers
		if ($.template.ie7 || $.template.ie8)
		{
			doc.find('.focus').not(element).blur();
		}

		// Show focus
		element.addClass('focus');

		/*
		 * Keyboard events handling
		 */
		handleKeysEvents = function(event)
		{
			if (event.keyCode == $.template.keys.space)
			{
				// If radio, do not allow uncheck as this may leave all radios unchecked
				if (!replaced.is(':radio') || !replaced[0].checked)
				{
					// Change replaced state, listener will update style
					replaced[0].checked = !replaced[0].checked;
					replaced.change();
				}
				event.preventDefault();
			}
		};

		// Blur function
		function onBlur()
		{
			// Remove styling
			element.removeClass('focus');

			// Clear data
			element.removeData('checkableBlurFunction');

			// Stop listening
			doc.off('keydown', handleKeysEvents);
			element.off('blur', onBlur);
		}

		// Store for external calls
		element.data('checkableBlurFunction', onBlur);

		// Start listening
		element.on('blur', onBlur);
		doc.on('keydown', handleKeysEvents);
	});

	// Textareas focus
	doc.on('focus', 'textarea', function(event)
	{
		var element = $(this);

		// IE7-8 focus handle is different from modern browsers
		if ($.template.ie7 || $.template.ie8)
		{
			doc.find('.focus').not(element).blur();
		}

		// Styling
		element.addClass('focus');

	}).on('blur', 'textarea', function()
	{
		$(this).removeClass('focus');
	});

	// Inputs focus
	doc.on('focus', 'input', function(event)
	{
		var input = $(this),
			replacement, wrapper,
			last;

		// Do not handle if disabled
		if (input.closest('.disabled').length > 0 || input.is(':disabled'))
		{
			event.preventDefault();
			return;
		}

		// For radios and focus, pass focus to replacement element
		if (this.type === 'radio' || this.type === 'checkbox')
		{
			replacement = input.data('replacement');

			// Update visual style
			if (replacement)
			{
				replacement.addClass('focus');
			}

			// Done, even if no replacement
			return;
		}

		// IE7-8 focus handle is different from modern browsers
		if ($.template.ie7 || $.template.ie8)
		{
			doc.find('.focus').not(input).blur();
		}

		// Placeholder polyfill
		if (!Modernizr.input.placeholder && input.attr('placeholder') && input.val() === input.attr('placeholder'))
		{
			input.removeClass('placeholder').val('');
		}

		// Look for wrapped inputs
		wrapper = input.closest('.input, .inputs');

		// If wrapped
		if (wrapper.length > 0)
		{
			// Styling
			wrapper.addClass('focus');

			// For number inputs
			if (wrapper.hasClass('number'))
			{
				// Watch keydown
				input.on('keydown.number', function(event)
				{
					// If up and down
					if (event.which === 38 || event.which === 40)
					{
						input.incrementNumber((event.which === 38), event.shiftKey);
					}
				});

				// Watch keyup
				input.on('keyup.number', function(event)
				{
					var value = input.val();

					// Only trigger change if the content has changed
					if (value === last)
					{
						return;
					}

					// Update slider
					input.trigger('change');

					// Store for next check
					last = value;
				});
			}
		}
		else
		{
			// Styling
			input.addClass('focus');
		}

	}).on('blur', 'input', function()
	{
		var input = $(this),
			replacement,
			wrapper;

		// Not for radios and checkboxes
		if (this.type === 'radio' || this.type === 'checkbox')
		{
			replacement = input.data('replacement');

			// Update visual style
			if (replacement)
			{
				replacement.removeClass('focus');
			}

			// Done, even if no replacement
			return;
		}

		// Placeholder polyfill
		if (!Modernizr.input.placeholder && input.attr('placeholder') && input.val() === '' && input.attr('type') != 'password')
		{
			input.addClass('placeholder').val(input.attr('placeholder'));
		}

		// Remove styling
		wrapper = input.closest('.focus');
		wrapper.removeClass('focus');

		// For number inputs
		if (wrapper.hasClass('number'))
		{
			// Stop watching keyboard events
			input.off('keydown.number').off('keyup.number');

			// Validate value
			input.setNumber(input.val());
		}
	});

	// Placehoder support
	if (!Modernizr.input.placeholder)
	{
		// Empty placehoder on form submit
		doc.on('submit', 'form', function(event)
		{
			$(this).find('input.placeholder').each(function()
			{
				var input = $(this);

				if (input.attr('placeholder') && input.val() === input.attr('placeholder'))
				{
					input.val('');
				}
			});
		});
	}

	// File inputs
	doc.on('change', '.file > input[type="file"]', function(event)
	{
		var input = $(this),
			files = [], text, i;

		// Update styling text
		if (this.multiple && this.files)
		{
			for (i = 0; i < this.files.length; i++)
			{
				files.push(this.files[i].name.split(/(\/|\\)/).pop());
			}
			text = files.join(', ');
		}
		else
		{
			text = input.val().split(/(\/|\\)/).pop();
		}

		// Set text
		input.siblings('.file-text').text(text);
	});

	// Value inputs
	doc.on('click', '.number-up, .number-down', function(event)
	{
		var button = $(this),
			wrapper = button.parent(),
			input = wrapper.children('input:first'),
			value;

		// Check if valid
		if (input.length === 0)
		{
			return;
		}

		// Increment
		input.incrementNumber(button.hasClass('number-up'), event.shiftKey).focus().trigger('change');
	});

	// Scroll on value inputs
	doc.on('mousewheel', '.number', function(event, delta, deltaX, deltaY)
	{
		// If the element scrolled
		$(this).incrementNumber(delta > 0, event.shiftKey).focus().trigger('change');

		// Prevent parents from scrolling
		event.preventDefault();
	});

	// Handle select focus
	if (!$.template.touchOs)
	{
		doc.on('focus', 'select', function()
		{
			var select = $(this).data('replacement');
			if (select)
			{
				select.focus();
			}

		});
	}
	doc.on('change', 'select', function()
	{
		var replaced = $(this),
			selected = replaced.find(':selected'),
			select = replaced.data('replacement'),
			values = [],
			displayAsMultiple, text, settings;

		// If valid
		if (select)
		{
			// Settings
			settings = select.data('select-settings');

			// Mode
			displayAsMultiple = select.hasClass('selectMultiple');

			// If nothing selected
			if (selected.length === 0)
			{
				// Update displayed value
				if (!displayAsMultiple)
				{
					// Get empty placeholder
					text = replaced.data('no-value-text') || settings.noValueText;

					// Must not be empty to preserve vertical-align
					if (!text || text.length === 0)
					{
						text = '&nbsp;';
					}

					// Set text
					select.children('.select-value').addClass('alt').html(text);
				}

				// If open, deselect all
				if (select.hasClass('open') || displayAsMultiple)
				{
					select.children('.drop-down').children('a, span').removeClass('selected');
				}
			}
			else
			{
				if (!displayAsMultiple)
				{
					// Gather selected values texts
					selected.each(function(i)
					{
						values.push($(this).text());
					});

					// Update displayed value
					if (this.multiple)
					{
						switch (values.length)
						{
							case 1:
								_updateSelectValueText(select, values, replaced.data('single-value-text'), settings.singleValueText);
								break;

							case this.options.length:
								_updateSelectValueText(select, values, replaced.data('all-values-text'), settings.allValuesText);
								break;

							default:
								_updateSelectValueText(select, values, replaced.data('multiple-values-text'), settings.multipleValuesText);
								break;
						}
					}
					else
					{
						select.children('.select-value').text((values.length > 0) ? values.join(', ') : '&nbsp;');
					}
				}

				// Update selected element
				select.children('.drop-down').children('a, span').each(function()
				{
					var option = $(this),
						selectValue = option.data('select-value');
					if (selectValue)
					{
						option[selectValue.selected ? 'addClass' : 'removeClass']('selected');
					}
				});
			}
		}
	});

	// Handle select focus
	doc.on('focus', 'span.select, span.selectMultiple', function(event)
	{
		// Only work if the element is the event's target
		if (event.target !== this)
		{
			return;
		}

		var select = $(this),
			settings = select.data('select-settings'),
			replaced = select.data('replaced'),
			list = select.children('.drop-down'),
			handleKeysEvents, search = '',
			blurTimeout, searchTimeout;

		// Do not handle if disabled
		if (select.closest('.disabled').length > 0 || (replaced && replaced.is(':disabled')))
		{
			event.preventDefault();
			return;
		}

		// Handle really close blur/focus events
		blurTimeout = select.data('selectBlurTimeout');
		if (blurTimeout)
		{
			// The select is still focused but about to blur, prevent and remain focused
			clearTimeout(blurTimeout);
			select.removeData('selectBlurTimeout');
			return;
		}

		// Do not handle if already focused
		if (select.hasClass('focus'))
		{
			return;
		}

		// Visual style
		select.addClass('focus');

		/**
		 * Keyboard events handling
		 */

		// Affect original element, listeners will update the replacement
		handleKeysEvents = function(event)
		{
			var keys = $.template.keys,
				closeFunc, selectedIndex, mode,
				focus, next, replacedOption,
				character, searchRegex;

			// If using easy multiple selection, use focus instead of selection
			mode = select.hasClass('easy-multiple-selection') ? 'focus' : 'selected';

			// Key handling
			switch (event.keyCode)
			{
				case keys.tab:
					// If open, close before tabultation triggers to preserve natural tabultation order
					closeFunc = select.data('selectCloseFunction');
					if (closeFunc)
					{
						closeFunc();
					}
					break;

				case keys.up:
					// If open or multiple, work on displayed options
					if (select.hasClass('open') || select.hasClass('selectMultiple'))
					{
						// Focused element
						focus = list.children('.'+mode+':first');
						if (focus.length === 0)
						{
							next = list.children('a:last, span:last');
						}
						else
						{
							next = focus.prevAll('a:first, span:first');
						}

						// Focus previous option
						if (next.length > 0)
						{
							focus.removeClass(mode);
							next.addClass(mode).scrollToReveal();

							// If selection mode, update replaced and trigger change
							if (mode === 'selected' && replaced)
							{
								replacedOption = next.data('select-value');
								if (replacedOption)
								{
									// If multiple selection, clear all before
									if (replaced[0].multiple)
									{
										replaced.find('option:selected').prop('selected', false);
									}

									replacedOption.selected = true;
									replaced.trigger('change');
								}
							}
						}

						event.preventDefault();
					}
					// If replacement
					else if (replaced)
					{
						// Update original, listeners will update the replacement
						selectedIndex = _getSelectedIndex(replaced);
						if (selectedIndex !== false && selectedIndex > 0)
						{
							replaced[0].selectedIndex = selectedIndex-1;
						}
						replaced.change();
						event.preventDefault();
					}
					break;

				case keys.down:
					// If not open yet, check if we have to
					if (select.hasClass('select') && !select.hasClass('open') && settings.openOnKeyDown)
					{
						_openSelect(select);
						event.preventDefault();
					}
					else
					{
						// If open or multiple, work on displayed options
						if (select.hasClass('open') || select.hasClass('selectMultiple'))
						{
							// Focused element
							focus = list.children('.'+mode+':last');
							if (focus.length === 0)
							{
								next = list.children('a:first, span:first');
							}
							else
							{
								next = focus.nextAll('a:first, span:first');
							}

							// Focus next option
							if (next.length > 0)
							{
								focus.removeClass(mode);
								next.addClass(mode).scrollToReveal();

								// If selection mode, update replaced and trigger change
								if (mode === 'selected' && replaced)
								{
									replacedOption = next.data('select-value');
									if (replacedOption)
									{
										// Set value
										replaced.val(replacedOption.value).trigger('change');
									}
								}
							}

							event.preventDefault();
						}
						// If replacement
						else if (replaced)
						{
							// Update original, listeners will update the replacement
							selectedIndex = _getSelectedIndex(replaced);
							if (selectedIndex !== false && selectedIndex < replaced[0].options.length-1)
							{
								replaced[0].selectedIndex = selectedIndex+1;
							}
							replaced.change();
							event.preventDefault();
						}
					}
					break;

				case keys.enter:
				case keys.space:
					// If focus mode on, simulate click
					if (mode === 'focus' && (select.hasClass('selectMultiple') || select.hasClass('open')))
					{
						// Focused element
						focus = list.children('.'+mode);
						if (focus.length === 1)
						{
							event.preventDefault();
							focus.click();
						}
					}
					// Else, just close the select if open
					else if (select.hasClass('open'))
					{
						closeFunc = select.data('selectCloseFunction');
						if (closeFunc)
						{
							closeFunc();
							event.preventDefault();
						}
					}
					break;

				default:
					// Get pressed key character
					character = String.fromCharCode(event.keyCode);

					// If regular character
					if (character && character.length === 1)
					{
						// If a search timeout is in, stop it
						if (searchTimeout)
						{
							clearTimeout(searchTimeout);
						}

						// Add to search
						search += character.toLowerCase();
						searchRegex = new RegExp('^'+search, 'g');

						// Start timeout to clear search string when no more key are pressed
						searchTimeout = setTimeout(function()
						{
							search = '';

						}, 1500);

						// Mode
						if (select.hasClass('open') || select.hasClass('selectMultiple'))
						{
							// Loop through values to find a match
							list.children('a, span').each(function(i)
							{
								var option = $(this);

								// If matches
								if ($.trim(option.text().toLowerCase()).match(searchRegex))
								{
									// Focused element
									focus = list.children('.'+mode+':last');

									// Focus option
									focus.removeClass(mode);
									option.addClass(mode).scrollToReveal();

									// If selection mode, update replaced and trigger change
									if (mode === 'selected' && replaced)
									{
										replacedOption = option.data('select-value');
										if (replacedOption)
										{
											// Set value
											replaced.val(replacedOption.value).trigger('change');
										}
									}

									// Prevent default key event
									event.preventDefault();

									// Stop search
									return false;
								}
							});
						}
						// Closed mode only works for replacements
						else if (replaced)
						{
							// Loop through values to find a match
							replaced.find('option').each(function(i)
							{
								// If matches
								if ($.trim($(this).text().toLowerCase()).match(searchRegex))
								{
									// Set value
									replaced.val(this.value).trigger('change');

									// Prevent default key event
									event.preventDefault();

									// Stop search
									return false;
								}
							});
						}
					}
					break;
			}
		};

		// Blur function
		function onBlur(event)
		{
			var closeFunc;

			// Handle really close blur/focus events
			blurTimeout = select.data('selectBlurTimeout');
			if (!blurTimeout)
			{
				// Wait, are you sure you want me to blur? Let's just wait a little...
				select.data('selectBlurTimeout', setTimeout(function() { onBlur.call(this, event); }, 40));
				return;
			}
			else
			{
				// The blur timeout has ended without getting back focus, so let's blur!
				select.removeData('selectBlurTimeout');
			}

			// Clear data
			select.removeData('selectBlurFunction');

			// Remove styling
			if (select.children('.select-search').children('input:focus').length === 0)
			{
				select.removeClass('focus');
			}
			list.children('.focus').removeClass('focus');

			// Stop listening
			doc.off('keydown', handleKeysEvents);
			select.off('blur', onBlur);
		}

		// Store for external calls
		select.data('selectBlurFunction', onBlur);

		// Start listening
		select.on('blur', onBlur);
		doc.on('keydown', handleKeysEvents);
	});

	// Opening when on touch device
	if ($.template.touchOs)
	{
		// Open on tap
		doc.on('touchend', '.select', function(event)
		{
			_openSelect($(this), false, event);
		});
	}
	else
	{
		// Selects opening arrow
		doc.on('click', '.select-arrow, span.select-value', function(event)
		{
			var select = $(this).parent();

			// Filter here rather than in the delegated event, a little bit faster overall
			if (!select.hasClass('auto-open'))
			{
				_openSelect($(this).parent(), false, event);
			}
		});

		// Auto-opening selects
		doc.on('mouseenter', '.select.auto-open', function(event)
		{
			_openSelect($(this), true, event);
		});
	}

	/*
	 * Form validation hooks:
	 * The replaced selects need to be un-hidden to be validated, then hidden back
	 */
	doc.on('jqv.form.validating', 'form', function(event)
	{
		var form = $(this),
			hidden = form.find('span.select > select, span.selectMultiple > select').filter(':hidden').show(),

			// Return to normal state
			validateEnd = function()
			{
				hidden.hide();
				form.off('jqv.form.result', validateEnd);
			};

		// Listen for end of validation
		form.on('jqv.form.result', validateEnd);
	});

})(jQuery, window, document);
/**
 *
 * '||''|.                            '||
 *  ||   ||    ....  .... ...   ....   ||    ...   ... ...  ... ..
 *  ||    || .|...||  '|.  |  .|...||  ||  .|  '|.  ||'  ||  ||' ''
 *  ||    || ||        '|.|   ||       ||  ||   ||  ||    |  ||
 * .||...|'   '|...'    '|     '|...' .||.  '|..|'  ||...'  .||.
 *                                                  ||
 * --------------- By Display:inline ------------- '''' -----------
 *
 * Accordions plugin
 *
 * Structural good practices from the article from Addy Osmani 'Essential jQuery plugin patterns'
 * @url http://coding.smashingmagazine.com/2011/10/11/essential-jquery-plugin-patterns/
 */

/*
 * The semi-colon before the function invocation is a safety
 * net against concatenated scripts and/or other plugins
 * that are not closed properly.
 */
;(function($, window, document)
{
	/*
	 * document is passed through as local variable rather than as global, because this (slightly)
	 * quickens the resolution process and can be more efficiently minified.
	 */

		// Objects cache
	var win = $(window),
		doc = $(document);

	// Event binding
	doc.on('click', '.accordion > dt', function(event)
	{
		var dt = $(this),
			dl = dt.parent();

		// Equalize height when needed
		if (dl.hasClass('.same-height'))
		{
			dl.refreshAccordion();
		}

		// Check if closed
		if (dt.hasClass('closed'))
		{
			// Close previous
			dl.children('dt').not('.closed').each(function(i)
			{
				$(this).addClass('closed').next('dd').stop(true).slideUp();
			});

			// Open
			dt.removeClass('closed');
			dt.next('dd').stop(true).slideDown();
		}
	});

	/**
	 * Refresh accordions height
	 */
	$.fn.refreshAccordion = function()
	{
		this.each(function(i)
		{
				// Accordions
			var dl = $(this).closest('.accordion'),
				sections = dl.children('dd'),

				// Hidden parents
				hidden,

				// Processing vars
				datas = [],
				maxHeight = 0;

			// If not found or not valid
			if (dl.length === 0 || !dl.hasClass('same-height') || sections.length === 0)
			{
				return;
			}

			// Reveal hidden parents if needed for correct height processing
			hidden = dl.tempShow();

			// Gather sections blocks and infos
			sections.each(function(i)
			{
				var section = $(this).show(),
					topMargin, height;

				// Get total height
				height = section.css('height', '').outerHeight();

				// Get first element's top-margin, because it affects the block height if it is negative
				topMargin = Math.min(0, section.children(':first').parseCSSValue('margin-top'));

				// Check if this is the tallest element
				maxHeight = Math.max(maxHeight, height+topMargin);

				// Store for equalization loop below
				datas[i] = [height, topMargin];
			});

			// Set equalized height
			sections.each(function(i)
			{
				var section = $(this),
					dt = section.prev(),
					height = datas[i][0],
					topMargin = datas[i][1];

				// Set height depending on margins
				section.height(maxHeight-(height-section.height())-topMargin);

				// Hide if not current one
				if (dt && dt.hasClass('closed'))
				{
					section.hide();
				}
			});

			// Hide previously hidden parents
			hidden.tempShowRevert();
		});

		return this;
	};

	// Add template setup function
	$.template.addSetupFunction(function(self, children)
	{
		var accordions = this.findIn(self, children, '.accordion');

		// Equalize height when needed
		accordions.filter('.same-height').refreshAccordion();

		// Show only active tab
		accordions.each(function(i)
		{
			var dts = $(this).children('dt'),
				active;

			// Active section
			active = dts.filter('.open');
			if (active.length === 0)
			{
				active = dts.not('.closed').first();
			}
			if (active.length === 0)
			{
				active = dts.first();
			}

			// Tag and show/hide
			active.removeClass('closed').next('dd').show();
			active.siblings('dt').addClass('closed').next('dd').hide();
		});

		return this;
	});

})(jQuery, window, document);
/**
 *
 * '||''|.                            '||
 *  ||   ||    ....  .... ...   ....   ||    ...   ... ...  ... ..
 *  ||    || .|...||  '|.  |  .|...||  ||  .|  '|.  ||'  ||  ||' ''
 *  ||    || ||        '|.|   ||       ||  ||   ||  ||    |  ||
 * .||...|'   '|...'    '|     '|...' .||.  '|..|'  ||...'  .||.
 *                                                  ||
 * --------------- By Display:inline ------------- '''' -----------
 *
 * Block messages plugin
 *
 * Structural good practices from the article from Addy Osmani 'Essential jQuery plugin patterns'
 * @url http://coding.smashingmagazine.com/2011/10/11/essential-jquery-plugin-patterns/
 */

/*
 * The semi-colon before the function invocation is a safety
 * net against concatenated scripts and/or other plugins
 * that are not closed properly.
 */
;(function($, undefined)
{
	/*
	 * undefined is used here as the undefined global variable in ECMAScript 3 is mutable (i.e. it can
	 * be changed by someone else). undefined isn't really being passed in so we can ensure that its value is
	 * truly undefined. In ES5, undefined can no longer be modified.
	 */

	/**
	 * Display a message on the target element
	 *
	 * @param string message the text or html message to display
	 * @param object options - optional (see defaults for a complete list)
	 * @return jQuery the messages nodes
	 */
	$.fn.message = function(message, options)
	{
		// Settings
		var globalSettings = $.extend({}, $.fn.message.defaults, options),
			all;

		// Insert message
		all = $();
		this.each(function(i)
		{
			var target = $(this),
				settings = $.extend({}, globalSettings, target.data('message-options')),
				classes = ['message'].concat(settings.classes),
				onTop = (settings.position.toLowerCase() != 'bottom'),
				method = onTop ? (settings.append ? 'prependTo' : 'insertBefore') : (settings.append ? 'appendTo' : 'insertAfter'),
				link = (settings.node.toLowerCase() === 'a') ? ' href="'+settings.link+'"' : '',

				// Extra elements
				simpler = settings.simpler ? ' simpler' : '',
				inset = settings.inset ? ' inset' : '',
				closeOnHover = settings.showCloseOnHover ? ' show-on-parent-hover' : '',
				closeButton = settings.closable ? '<span class="close'+inset+closeOnHover+simpler+'">✕</span>' : '',
				useArrow = (settings.arrow && $.inArray(settings.arrow.toLowerCase(), ['top', 'right', 'bottom', 'left']) > -1),
				arrow = useArrow ? '<span class="block-arrow '+settings.arrow.toLowerCase()+'"><span></span></span>' : '',

				// Other vars
				stripesSize, animatedStripes, darkStripes, stripes = '',
				element, previous, found = false, count;

			// If similar messages should be grouped
			if (settings.groupSimilar)
			{
				// Gather previous messages
				if (settings.append)
				{
					previous = target.childrenImmediates('.message', !onTop).not('.closing');
				}
				else
				{
					previous = target[onTop ? 'prevImmediates' : 'nextImmediates']('.message').not('.closing');
				}

				// Check if a similar message exists
				previous.each(function(i)
				{
					var element = $(this);
					if (element.data('message-text') === message)
					{
						found = element;
						return false;
					}
				});
				if (found)
				{
					// Count
					if (settings.groupCount)
					{
						// Check if count element already exists
						count = found.children('.count');
						if (count.length > 0)
						{
							count.text((parseInt(count.text()) || 1)+1);
						}
						else
						{
							found.append('<span class="count left'+inset+'">2</span>');
						}
					}

					// Effect
					found.shake();

					all = all.add(found);
					return found;
				}
			}

			// Stripes
			if (settings.stripes)
			{
				// Dark or not
				darkStripes = settings.darkStripes ? 'dark-' : '';

				// Size
				stripesSize = (settings.stripesSize === 'big' || settings.stripesSize === 'thin') ? settings.stripesSize+'-' : '';

				// Animated
				animatedStripes = settings.animatedStripes ? ' animated' : '';

				// Final
				stripes = '<span class="'+darkStripes+stripesSize+'stripes'+animatedStripes+'"></span>';
			}

			// Insert
			element = $('<'+settings.node+link+' class="'+classes.join(' ')+simpler+'">'+stripes+message+closeButton+arrow+'</'+settings.node+'>')[method](target);

			// Store message for later comparisons
			element.data('message-text', message);

			// Add to selections
			target.data('messages', (target.data('messages') || $()).add(element));
			all = all.add(element);

			// Effect
			if (settings.animate)
			{
				element.hide().slideDown(settings.animateSpeed);
			}
		});

		return all;
	};

	/**
	 * Clear element's message(s)
	 *
	 * @param string message the message to remove (can be omitted)
	 * @param boolean animate use an animation (foldAndRemove) to remove the messages (default: false)
	 * @return jQuery the chain
	 */
	$.fn.clearMessages = function(message, animate)
	{
		// Params
		if (typeof message === 'boolean')
		{
			animate = message;
			message = '';
		}
		animate = (animate || animate == undefined);

		this.each(function(i)
		{
			var messages = $(this).data('messages'),
				removed;
			if (messages)
			{
				// If specific message only
				if (message && message.length > 0)
				{
					removed = $();
					messages.each(function(i)
					{
						if ($(this).data('message-text') === message)
						{
							removed = removed.add(this);
						}
					});
				}
				else
				{
					// Remove all
					removed = messages;
				}

				// Remove
				removed.addClass('closing')[animate ? 'foldAndRemove' : 'remove']();

				// Update/clear data
				if (removed.length === messages.length)
				{
					$(this).removeData('messages');
				}
				else
				{
					$(this).data('messages', messages.not(removed));
				}
			}
		});

		return this;
	};

	/**
	 * Message function defaults
	 * @var object
	 */
	$.fn.message.defaults = {

		/**
		 * Whether to append the message element or to insert it next to the target
		 * @var boolean
		 */
		append: true,

		/**
		 * Position in or next the target: 'top' or 'bottom'
		 * @var string
		 */
		position: 'top',

		/**
		 * Arrow direction or false for none
		 * @var string|boolean
		 */
		arrow: false,

		/**
		 * Node type for the message (tip: 'p' has bottom-margin, 'a' and 'div' have none)
		 * @var string
		 */
		node: 'p',

		/**
		 * Link when the node type is 'a'
		 * @var string
		 */
		link: '#',

		/**
		 * Extra classes (colors...)
		 * @var array
		 */
		classes: [],

		/**
		 * Enable animated stripes
		 * @var boolean
		 */
		stripes: false,

		/**
		 * True for animated stripes (only on compatible browsers)
		 * @var boolean
		 */
		animatedStripes: true,

		/**
		 * True for dark stripes, false for white stripes
		 * @var boolean
		 */
		darkStripes: true,

		/**
		 * Stripes size: 'big', 'normal' or 'thin'
		 * @var string
		 */
		stripesSize: 'normal',

		/**
		 * Use true to remove rounded corners and bewel
		 * @var boolean
		 */
		simpler: false,

		/**
		 * Enable a close button
		 * @var boolean
		 */
		closable: true,

		/**
		 * Show the close button only on hover
		 * @var boolean
		 */
		showCloseOnHover: true,

		/**
		 * Animate the message's occurrence
		 * @var boolean
		 */
		animate: true,

		/**
		 * Speed of animation (any jQuery valid value)
		 * @var string|int
		 */
		animateSpeed: 'fast',

		/**
		 * Group similar messages
		 * @var boolean
		 */
		groupSimilar: true,

		/**
		 * Display a bubble with the count of grouped messages
		 * @var boolean
		 */
		groupCount: true,

		/**
		 * Should close and count bubbles be inside the message?
		 * @var boolean
		 */
		inset: false

	};

})(jQuery);
/**
 *
 * '||''|.                            '||
 *  ||   ||    ....  .... ...   ....   ||    ...   ... ...  ... ..
 *  ||    || .|...||  '|.  |  .|...||  ||  .|  '|.  ||'  ||  ||' ''
 *  ||    || ||        '|.|   ||       ||  ||   ||  ||    |  ||
 * .||...|'   '|...'    '|     '|...' .||.  '|..|'  ||...'  .||.
 *                                                  ||
 * --------------- By Display:inline ------------- '''' -----------
 *
 * Modal window plugin
 *
 * Structural good practices from the article from Addy Osmani 'Essential jQuery plugin patterns'
 * @url http://coding.smashingmagazine.com/2011/10/11/essential-jquery-plugin-patterns/
 */

/*
 * The semi-colon before the function invocation is a safety
 * net against concatenated scripts and/or other plugins
 * that are not closed properly.
 */
;(function($, window, document)
{
	/*
	 * document is passed through as local variable rather than as global, because this (slightly)
	 * quickens the resolution process and can be more efficiently minified.
	 */

		// Objects cache
	var win = $(window),
		doc = $(document),

		// Viewport dimensions
		viewportWidth = $.template.viewportWidth,
		viewportHeight = $.template.viewportHeight;

	// Update on viewport resize
	win.on('normalized-resize orientationchange', function()
	{
		// Previous viewport dimensions
		var previousWidth = viewportWidth,
			previousHeight = viewportHeight,
			widthChange, heightChange;

		// New dimensions
		viewportWidth = $.template.viewportWidth;
		viewportHeight = $.template.viewportHeight;

		// Size changes
		widthChange = Math.round((viewportWidth-previousWidth)/2);
		heightChange = Math.round((viewportHeight-previousHeight)/2);

		// Check windows size/position
		$.modal.all.each(function(i)
		{
			var modal = $(this),
				data = modal.data('modal');

			// If valid
			if (data)
			{
				// Update max-sizes
				data.updateMaxSizes();

				// Redefine position relative to screen center
				data.setPosition(
					modal.parseCSSValue('left')+widthChange,
					modal.parseCSSValue('top')+heightChange
				);
			}
		});
	});

	/**
	 * Return the modal windows root div
	 * @return jQuery the jQuery object of the root div
	 */
	function getModalRoot()
	{
		var root = $('#modals');
		if (root.length == 0)
		{
			// Create element
			root = $('<div id="modals"></div>').appendTo(document.body);

			// Add to position:fixed fallback
			if ($.fn.enableFixedFallback)
			{
				root.enableFixedFallback();
			}
		}

		return root;
	};

	/**
	 * Opens a new modal window
	 * @param object options an object with any of the $.modal.defaults options.
	 * @return object the jQuery object of the new window
	 */
	$.modal = function(options)
	{
		var settings = $.extend({}, $.modal.defaults, options),
			root = getModalRoot(),

			// Elements
			modal, barBlock, contentBg, contentBlock,
			actionsBlock = false, buttonsBlock = false,

			// Max sizes
			maxWidth, maxHeight,

			// Blocker layer
			wasBlocked, blocker = false,

			// DOM content
			dom, domHidden = false, placeholder,

			// Vars for handleResize and handleMove
			modalX = 0,
			modalY = 0,
			contentWidth = 0,
			contentHeight = 0,
			mouseX = 0,
			mouseY = 0,
			resized,
			handleResize, endResize,
			handleMove, endMove,

			// Vars for markup building
			title = settings.title ? '<h3>'+settings.title+'</h3>' : '',
			titleBar = (settings.titleBar || (settings.titleBar === null && title.length > 0)) ? '<div class="modal-bar">'+title+'</div>' : '',
			sizeParts = new Array(), contentWrapper,
			spacingClass = '',

			/**
			 * Remove DOM content
			 * @return void
			 */
			removeDom = function()
			{
				// If DOM content is on
				if (dom)
				{
					// If pulled from the dom
					if (placeholder)
					{
						dom.detach().insertAfter(placeholder);
						placeholder.remove();
					}

					// If hidden
					if (domHidden)
					{
						dom.hide();
					}

					// Reset
					dom = false;
					domHidden = false;
					placeholder = false;
				}
			},

			/**
			 * Set content, eventually wrapping it in beforeContent/afterContent if needed
			 * @param string|jQuery content the conntent to append
			 * @return void
			 */
			setContent = function(content)
			{
				var domWrapper;

				// Not available for iframes
				if (settings.useIframe)
				{
					return;
				}

				// Remove existing content
				removeDom();
				contentBlock.empty();

				if (typeof(content) !== 'string')
				{
					// Use dom content
					dom = settings.content;

					// This is required to handle DOM insertion when using beforeContent/afterContent
					content = '<span class="modal-dom-wrapper"></span>';

					// If hidden
					if (!dom.is(':visible'))
					{
						domHidden = true;
						dom.show();
					}

					// Check if already in the document
					if (dom.parent().length > 0)
					{
						placeholder = $('<span style="display:none"></span>').insertBefore(dom);
						dom.detach();
					}
				}

				// Insert
				contentBlock.append(settings.beforeContent+content+settings.afterContent);

				// DOM
				if (dom)
				{
					// Retrieve placeholder
					domWrapper = contentBlock.find('.modal-dom-wrapper');

					// Insert
					dom.insertAfter(domWrapper);

					// Remove placeholder
					domWrapper.remove();
				}
			},

			/**
			 * Set window content-block size
			 * @param int|boolean width the width to set, true to keep current or false for fluid width (false only works if not iframe)
			 * @param int|boolean height the height to set, true to keep current or false for fluid height (false only works if not iframe)
			 * @return void
			 */
			setContentSize = function(width, height)
			{
				var scrollX, scrollY, css = {};

				// If nothing changes
				if (width === true && height === true)
				{
					return;
				}

				// Mode
				if (settings.useIframe)
				{
					if (typeof width === 'number')
					{
						contentBlock.prop('width', Math.min(width, maxWidth));
					}
					if (typeof height === 'number')
					{
						contentBlock.prop('height', Math.min(height, maxHeight));
					}
				}
				else
				{
					// If width change
					if (width !== true)
					{
						// Apply width first
						contentBlock.css({
							width: width ? width+'px' : ''
						});

						// Refresh tabs if any
						if ($.fn.refreshInnerTabs)
						{
							contentBlock.refreshInnerTabs();
						}

						// Check if everything fits
						scrollX = contentBlock.prop('scrollWidth');
						if (scrollX > width)
						{
							contentBlock.css({
								width: scrollX+'px'
							});
						}
					}

					// Then set height
					if (width !== true)
					{
						contentBlock.css({
							height: height ? height+'px' : ''
						});
					}

					// Check if everything fits
					scrollY = contentBlock.prop('scrollHeight');
					if (scrollY > height)
					{
						contentBlock.css({
							height: scrollY+'px'
						});
					}
				}
			},

			/**
			 * Set modal position
			 * @param int x the horizontal position
			 * @param int y the vertical position
			 * @return void
			 */
			setPosition = function(x, y, animate)
			{
				// Set position
				modal[animate ? 'animate' : 'css']({
					left:	Math.min(Math.max(0, x), viewportWidth-modal.outerWidth()),
					top:	Math.min(Math.max(0, y), viewportHeight-modal.outerHeight())
				});
			},

			/**
			 * Load ajax content or set iframe url
			 * @param string url the url to load
			 * @param object options options for AJAX loading (ignored if using iFrame)
			 * @return void
			 */
			loadContent = function(url, options)
			{
				// Mode
				if (settings.useIframe)
				{
					contentBlock.prop('src', url);
				}
				else
				{
					// Settings with local scope callbacks
					var ajaxOptions = $.extend({}, $.modal.defaults.ajax, options, {

						// Handle loaded content
						success: function(data, textStatus, jqXHR)
						{
							// Set content
							setContent(data);

							// Resize
							if (ajaxOptions.resize || ajaxOptions.resizeOnLoad)
							{
								setContentSize(true, false);
							}

							// Call user callback
							if (options.success)
							{
								options.success.call(this, data, textStatus, jqXHR);
							}
						}

					});

					// If no error callback
					if (!ajaxOptions.error && ajaxOptions.errorMessage)
					{
						ajaxOptions.error = function(jqXHR, textStatus, errorThrown)
						{
							setContent(ajaxOptions.errorMessage);
							if (ajaxOptions.resize || ajaxOptions.resizeOnMessage)
							{
								setContentSize(true, false);
							}
						}
					}

					// If loading message
					if (ajaxOptions.loadingMessage)
					{
						setContent(ajaxOptions.loadingMessage);
						if (ajaxOptions.resize || ajaxOptions.resizeOnMessage)
						{
							setContentSize(true, false);
						}
					}

					// Load content
					$.ajax(url, ajaxOptions);
				}
			},

			/**
			 * Set the modal title, creating/removing elements as needed
			 * @param string title the new title, or false/empty string for no title
			 * @return void
			 */
			setTitle = function(title)
			{
				var h3;

				// If no title bar, quit
				if (settings.titleBar === false)
				{
					return;
				}

				// If set
				if (typeof title === 'string' && title.length > 0)
				{
					// If the is no title bar yet
					if (!barBlock)
					{
						// Create
						barBlock = $('<div class="modal-bar"><h3>'+title+'</h3></div>').prependTo(modal);

						// If there are action leds, move them to the title bar
						if (actionsBlock)
						{
							actionsBlock.detach().prependTo(barBlock);
						}
					}
					else
					{
						// Find the title tag
						h3 = barBlock.children('h3');
						if (h3.length === 0)
						{
							h3 = $('<h3></h3>').appendTo(barBlock);
						}

						// Set title
						h3.html(title);
					}
				}
				else
				{
					// If there is already a title bar
					if (barBlock)
					{
						// If there are action leds, move them to the modal
						if (actionsBlock)
						{
							actionsBlock.detach().prependTo(modal);
						}

						// Remove bar
						barBlock.remove();
						barBlock = false;
					}
				}
			},

			/**
			 * Close the modal
			 * @return void
			 */
			closeModal = function()
			{
				// Close callback
				if (settings.onClose)
				{
					if (settings.onClose.call(modal[0]) === false)
					{
						return;
					}
				}

				// Blocker
				if (blocker)
				{
					blocker.removeClass('visible');
				}

				// Fade then remove
				modal.stop(true).animate({
					'opacity': 0,
					'marginTop': '-30px'
				}, 300, function()
				{
					// Dom
					if (dom)
					{
						// If pulled from the dom
						if (placeholder)
						{
							dom.detach().insertAfter(placeholder);
							placeholder.remove();
						}

						// If hidden
						if (domHidden)
						{
							dom.hide();
						}
					}

					// Remove
					modal.remove();

					// Blocker
					if (blocker)
					{
						blocker.remove();
						if (root.children('.modal-blocker').length === 0)
						{
							root.removeClass('with-blocker');

							// Update position for fixed elements fallback
							if ($.fn.detectFixedBounds)
							{
								root.detectFixedBounds();
							}
						}
					}
				});

				// Remaining modals
				$.modal.all = modal.siblings('.modal');
				if ($.modal.all.length == 0)
				{
					// No more modals
					$.modal.current = false;
				}
				else
				{
					// Refresh current
					$.modal.current = $.modal.all.last();
				}
			},

			/**
			 * Update content-block max siezs, according to viewport size and pre-defined max width/height
			 * @return void
			 */
			updateMaxSizes = function()
			{
				var viewportMaxWidth = viewportWidth-(2*settings.maxSizeMargin)-(modal.outerWidth()-contentBlock.width()),
					viewportMaxHeight = viewportHeight-(2*settings.maxSizeMargin)-(modal.outerHeight()-contentBlock.height()),

					// Minimum sizes
					minWidth, minHeight;

				// maxWidth and maxHeight are set outside this function's scope, because they are used in setContentSize()

				// Get lowest values
				maxWidth = settings.maxWidth ? Math.min(settings.maxWidth, viewportMaxWidth) : viewportMaxWidth;
				maxHeight = settings.maxHeight ? Math.min(settings.maxHeight, viewportMaxHeight) : viewportMaxHeight;

				// Update content-block
				if (settings.useIframe)
				{
					contentBlock.prop('width', Math.min(settings.width, maxWidth));
					contentBlock.prop('height', Math.min(settings.height, maxHeight));
				}
				else
				{
					// Minimum size also needs to be within viewport range
					minWidth = settings.minWidth ? Math.min(settings.minWidth, viewportMaxWidth) : viewportMaxWidth;
					minHeight = settings.minHeight ? Math.min(settings.minHeight, viewportMaxHeight) : viewportMaxHeight;

					// Update
					contentBlock.css({
						maxWidth: maxWidth+'px',
						maxHeight: maxHeight+'px',
						minWidth: minWidth+'px',
						minHeight: minHeight+'px'
					});
				}
			};

		// Blocker
		if (settings.blocker)
		{
			// Create
			wasBlocked = root.hasClass('with-blocker');
			blocker = $('<div class="modal-blocker"></div>').appendTo(root.addClass('with-blocker'));

			// Update position for fixed elements fallback
			if (!wasBlocked && $.fn.detectFixedBounds)
			{
				root.detectFixedBounds();
			}

			// Make it visible
			if (settings.blockerVisible)
			{
				// Adding the class afterwards will trigger the CSS animation
				blocker.addClass('visible');
			}
		}

		// If iframe
		if (settings.useIframe)
		{
			// Content size
			if (!settings.width)
			{
				settings.width = settings.maxWidth || settings.minWidth || 120;
			}
			if (!settings.height)
			{
				settings.height = settings.maxHeight || settings.minHeight || 120;
			}

			// Bloc style
			contentWrapper = '<iframe class="modal-iframe" src="'+(settings.url || '')+'" frameborder="0" width="'+settings.width+'" height="'+settings.height+'"></iframe>';
		}
		else
		{
			// Content size
			if (settings.minWidth)
			{
				sizeParts.push('min-width:'+settings.minWidth+'px;');
			}
			if (settings.minHeight)
			{
				sizeParts.push('min-height:'+settings.minHeight+'px;');
			}
			if (settings.width)
			{
				sizeParts.push('width:'+settings.width+'px; ');
			}
			if (settings.height)
			{
				sizeParts.push('height:'+settings.height+'px; ');
			}
			if (settings.maxWidth)
			{
				sizeParts.push('max-width:'+settings.maxWidth+'px; ');
			}
			if (settings.maxHeight)
			{
				sizeParts.push('max-height:'+settings.maxHeight+'px; ');
			}

			// Bloc style
			contentWrapper = '<div class="modal-content'+
							 (settings.scrolling ? ' modal-scroll' : '')+
							 ((settings.contentAlign !== 'left') ? ' align-'+settings.contentAlign : '')+
							 '" style="'+sizeParts.join(' ')+'"></div>';
		}

		// Insert window
		modal = $('<div class="modal"></div>').appendTo(root);
		barBlock = (titleBar.length > 0) ? $(titleBar).appendTo(modal) : false;
		contentBg = settings.contentBg ? $('<div class="modal-bg"></div>').appendTo(modal) : false;
		contentBlock = $(contentWrapper).appendTo(contentBg || modal);

		// Set contents
		if (!settings.useIframe && settings.content)
		{
			setContent(settings.content);
		}

		// Custom scroll
		if (!settings.useIframe && $.fn.customScroll)
		{
			contentBlock.customScroll();
		}

		// If resizable
		if (settings.resizable)
		{
			// Set new size
			handleResize = function(event)
			{
					// Mouse offset
				var offsetX = event.pageX-mouseX,
					offsetY = event.pageY-mouseY,

					// New size
					newWidth = Math.max(settings.minWidth, contentWidth+(resized.width*offsetX)),
					newHeight = Math.max(settings.minHeight, contentHeight+(resized.height*offsetY)),

					// Position correction
					correctX = 0,
					correctY = 0;

				// If max sizes are defined
				if (settings.maxWidth && newWidth > settings.maxWidth)
				{
					correctX = newWidth-settings.maxWidth;
					newWidth = settings.maxWidth;
				}
				if (settings.maxHeight && newHeight > settings.maxHeight)
				{
					correctY = newHeight-settings.maxHeight;
					newHeight = settings.maxHeight;
				}

				// Set size
				setContentSize(newWidth, newHeight);

				// Position
				setPosition(modalX+(resized.left*(offsetX+correctX)), modalY+(resized.top*(offsetY+correctY)));
			};

			// Callback on end of resize
			endResize = function(event)
			{
				doc.off('mousemove', handleResize)
				   .off('mouseup', endResize);
			};

			// Create resize handlers
			$('<div class="modal-resize-nw"></div>').appendTo(modal).data('modal-resize', {
				top: 1, left: 1,
				height: -1, width: -1

			}).add(
				$('<div class="modal-resize-n"></div>').appendTo(modal).data('modal-resize', {
					top: 1, left: 0,
					height: -1, width: 0
				})
			).add(
				$('<div class="modal-resize-ne"></div>').appendTo(modal).data('modal-resize', {
					top: 1, left: 0,
					height: -1, width: 1
				})
			).add(
				$('<div class="modal-resize-e"></div>').appendTo(modal).data('modal-resize', {
					top: 0, left: 0,
					height: 0, width: 1
				})
			).add(
				$('<div class="modal-resize-se"></div>').appendTo(modal).data('modal-resize', {
					top: 0, left: 0,
					height: 1, width: 1
				})
			).add(
				$('<div class="modal-resize-s"></div>').appendTo(modal).data('modal-resize', {
					top: 0, left: 0,
					height: 1, width: 0
				})
			).add(
				$('<div class="modal-resize-sw"></div>').appendTo(modal).data('modal-resize', {
					top: 0, left: 1,
					height: 1, width: -1
				})
			).add(
				$('<div class="modal-resize-w"></div>').appendTo(modal).data('modal-resize', {
					top: 0, left: 1,
					height: 0, width: -1
				})
			).mousedown(function(event)
			{
				// Detect positions
				contentWidth = contentBlock.width();
				contentHeight = contentBlock.height();
				var position = modal.position();
				modalX = position.left;
				modalY = position.top;

				// Mouse
				mouseX = event.pageX;
				mouseY = event.pageY;
				resized = $(this).data('modal-resize');

				// Prevent text selection
				event.preventDefault();

				doc.on('mousemove', handleResize)
				   .on('mouseup', endResize);

			}).on('selectstart', _preventTextSelectionIE); // Prevent text selection for IE7
		}

		// If movable
		if (settings.draggable)
		{
			// Set position
			handleMove = function(event)
			{
				// New position
				setPosition(modalX+(event.pageX-mouseX), modalY+(event.pageY-mouseY));
			};

			// Callback on end of move
			endMove = function(event)
			{
				doc.off('mousemove', handleMove)
				   .off('mouseup', endMove);
			};

			// Watch
			// Delegating the event to the modal allows the remove/add the title bar without handling this each time
			modal.on('mousedown', '.modal-bar', function(event)
			{
				// Detect positions
				var position = modal.position();
				modalX = position.left;
				modalY = position.top;
				mouseX = event.pageX;
				mouseY = event.pageY;

				// Prevent text selection
				event.preventDefault();

				// Listeners
				doc.on('mousemove', handleMove)
				   .on('mouseup', endMove);

			}).on('selectstart', '.modal-bar', _preventTextSelectionIE); // Prevent text selection for IE7
		}

		// Put in front
		modal.mousedown(function()
		{
			modal.putModalOnFront();
		});

		// Action leds
		$.each(settings.actions, function(name, config)
		{
			// Format
			if (typeof(config) === 'function')
			{
				config = {
					click: config
				};
			}

			// Button zone
			if (!actionsBlock)
			{
				actionsBlock = $('<ul class="modal-actions children-tooltip"></ul>').prependTo(barBlock || modal)
								.data('tooltip-options', settings.actionsTooltips);
			}

			// Insert
			$('<li'+(config.color ? ' class="'+config.color+'-hover"' : '')+'><a href="#" title="'+name+'">'+name+'</a></li>').appendTo(actionsBlock).children('a').click(function(event)
			{
				event.preventDefault();
				config.click.call(this, $(this).closest('.modal'), event);
			});
		});

		// Bottom buttons
		$.each(settings.buttons, function(name, config)
		{
			// Format
			if (typeof(config) === 'function')
			{
				config = {
					click: config
				};
			}

			// Button zone
			if (!buttonsBlock)
			{
				buttonsBlock = $('<div class="modal-buttons align-'+settings.buttonsAlign+(settings.buttonsLowPadding ? ' low-padding' : '')+'"></div>').insertAfter(contentBlock);
			}
			else
			{
				// Spacing
				spacingClass = ' mid-margin-left';
			}

			// Insert
			$('<button type="button" class="button'+(config.classes ? ' '+config.classes : '')+spacingClass+'">'+name+'</button>').appendTo(buttonsBlock).click(function(event)
			{
				config.click.call(this, $(this).closest('.modal'), event);
			});
		});

		// Update max sizes
		updateMaxSizes();

		// Interface
		modal.data('modal', {
			contentBlock:		contentBlock,
			setContent:			setContent,
			load:				loadContent,
			setContentSize:		setContentSize,
			setPosition:		setPosition,
			setTitle:			setTitle,
			close:				closeModal,
			updateMaxSizes:		updateMaxSizes
		});

		// Center and display effect
		modal.centerModal().css({
			'opacity': 0,
			'marginTop': '-30px'
		}).animate({
			'opacity': 1,
			'marginTop': '10px'
		}, 200).animate({
			'marginTop': 0
		}, 100);

		// Store as current
		$.modal.current = modal;
		$.modal.all = root.children('.modal');

		// Callback
		if (settings.onOpen)
		{
			settings.onOpen.call(modal[0]);
		}

		// If content url
		if (!settings.useIframe && settings.url)
		{
			loadContent(settings.url, settings.ajax);
		}

		return modal;
	};

	/**
	 * Internal function: used to prevent text selection under IE (event distint from 'mousedown')
	 *
	 * @return void
	 */
	function _preventTextSelectionIE(event)
	{
		event.preventDefault();
	}

	/**
	 * Shortcut to the current window, or false if none
	 * @var jQuery|boolean
	 */
	$.modal.current = false;

	/**
	 * jQuery selection of all open modal windows
	 * @var jQuery
	 */
	$.modal.all = $();

	/**
	 * Display an alert message
	 * @param string message the message, as text or html
	 * @param object options same as $.modal() (optional)
	 * @return jQuery the new window
	 */
	$.modal.alert = function(message, options)
	{
		options = options || {};
		$.modal($.extend({}, $.modal.defaults.alertOptions, options, {

			content: message

		}));
	};

	/**
	 * Display a prompt
	 * @param string message the message, as text or html
	 * @param function callback the function called with the user value: function(value). Can return false to prevent close.
	 * @param function cancelCallback a callback for when the user closes the modal or click on Cancel. Can return false to prevent close.
	 * @param object options same as $.modal() (optional)
	 * @return jQuery the new window
	 */
	$.modal.prompt = function(message, callback, cancelCallback, options)
	{
		// Params
		if (typeof cancelCallback !== 'function')
		{
			options = cancelCallback;
			cancelCallback = null;
		}
		options = options || {};

		// Cancel callback
		var isSubmitted = false, onClose;
		if (cancelCallback)
		{
			onClose = options.onClose;
			options.onClose = function(event)
			{
				// Check
				if (!isSubmitted && cancelCallback.call(this) === false)
				{
					return false;
				}

				// Previous onClose, if any
				if (onClose)
				{
					onClose.call(this, event);
				}
			};
		}

		// Open modal
		$.modal($.extend({}, $.modal.defaults.promptOptions, options, {

			content:	'<div class="margin-bottom">'+message+'</div><div class="input full-width"><input type="text" name="prompt-value" id="prompt-value" value="" class="input-unstyled full-width"></div>',
			buttons:	{

				'Cancel' : {
					classes :	'glossy',
					click :		function(modal) { modal.closeModal(); }
				},

				'Submit' : {
					classes :	'blue-gradient glossy',
					click :		function(modal)
					{
						// Mark as sumbmitted to prevent the cancel callback to fire
						isSubmitted = true;

						// Callback
						if (callback.call(modal[0], modal.find('input:first').val()) === false)
						{
							return;
						}

						// Close modal
						modal.closeModal();
					}
				}

			}

		}));
	};

	/**
	 * Display a confirm prompt
	 * @param string message the message, as text or html
	 * @param function confirmCallback the function called when hitting confirm
	 * @param function cancelCallback the function called when hitting cancel or closing the modal
	 * @param object options same as $.modal() (optional)
	 * @return jQuery the new window
	 */
	$.modal.confirm = function(message, confirmCallback, cancelCallback, options)
	{
		options = options || {};

		// Cancel callback
		var isConfirmed = false,
			onClose = options.onClose;
		options.onClose = function(event)
		{
			// Cancel callback
			if (!isConfirmed)
			{
				cancelCallback.call(this);
			}

			// Previous onClose, if any
			if (onClose)
			{
				onClose.call(this, event);
			}
		};

		// Open modal
		$.modal($.extend({}, $.modal.defaults.confirmOptions, options, {

			content:	message,
			buttons:	{

				'Cancel' : {
					classes:	'glossy',
					click:		function(modal) { modal.closeModal(); }
				},

				'Confirm' : {
					classes:	'blue-gradient glossy',
					click:		function(modal)
					{
						// Mark as sumbmitted to prevent the cancel callback to fire
						isConfirmed = true;

						// Callback
						confirmCallback.call(modal[0]);

						// Close modal
						modal.closeModal();
					}
				}

			}

		}));
	};

	/**
	 * Wraps the selected elements content in a new modal window.
	 * Some options can be set using the inline html5 data-modal-options attribute:
	 * <div data-modal-options="{'title':'Modal window title'}">Modal content</div>
	 * @param object options same as $.modal()
	 * @return jQuery the new window
	 */
	$.fn.modal = function(options)
	{
		var modals = $();

		this.each(function()
		{
			var element = $(this);
			modals.add($.modal($.extend({}, options, element.data('modal-options'), { content: element })));
		});

		return modals;
	};

	/**
	 * Use this method to retrieve the content div in the modal window
	 */
	$.fn.getModalContentBlock = function()
	{
		if (this.hasClass('.modal-content'))
		{
			return this;
		}

		var data = this.getModalWindow().data('modal');
		return data ? data.contentBlock : $();
	};

	/**
	 * Use this method to retrieve the modal window from any element within it
	 */
	$.fn.getModalWindow = function()
	{
		return this.closest('.modal');
	};

	/**
	 * Set window content (only if not using iframe)
	 * @param string|jQuery content the content to put: HTML or a jQuery object
	 * @param boolean resize use true to resize window to fit content (height only)
	 */
	$.fn.setModalContent = function(content, resize)
	{
		this.each(function()
		{
			var modal = $(this).getModalWindow(),
				data = (modal.length > 0) ? modal.data('modal') : false;

			// If valid
			if (data)
			{
				data.setContent(content);

				// Resizing
				if (resize)
				{
					data.setContentSize(true, false);
				}
			}
		});

		return this;
	};

	/**
	 * Set window content-block size
	 * @param int|boolean width the width to set, true to keep current or false for fluid width (false only works if not iframe)
	 * @param int|boolean height the height to set, true to keep current or false for fluid height (false only works if not iframe)
	 */
	$.fn.setModalContentSize = function(width, height)
	{
		this.each(function()
		{
			var modal = $(this).getModalWindow(),
				data = (modal.length > 0) ? modal.data('modal') : false;

			// If valid
			if (data)
			{
				data.setContentSize(width, height);
			}
		});

		return this;
	}

	/**
	 * Load AJAX content
	 * @param string url the content url
	 * @param object options (see defaults.ajax for details)
	 */
	$.fn.loadModalContent = function(url, options)
	{
		var settings = $.extend({}, $.modal.defaults.ajax, options)

		this.each(function()
		{
			var modal = $(this).getModalWindow(),
				data = (modal.length > 0) ? modal.data('modal') : false;

			// If valid
			if (data)
			{
				data.load(url, settings);
			}
		});

		return this;
	}

	/**
	 * Set modal title
	 * Note: if the option titleBar was set to false on opening, this will have no effect
	 * @param string title the new title (may contain HTML), or an empty string to remove the title
	 */
	$.fn.setModalTitle = function(title)
	{
		this.each(function()
		{
			var modal = $(this).getModalWindow(),
				data = (modal.length > 0) ? modal.data('modal') : false;

			// If valid
			if (data)
			{
				data.setTitle(title);
			}
		});

		return this;
	}

	/**
	 * Center the modal
	 * @param boolean animate true to animate the window movement
	 */
	$.fn.centerModal = function(animate)
	{
		this.each(function()
		{
			var modal = $(this).getModalWindow(),
				data = (modal.length > 0) ? modal.data('modal') : false;

			// If valid
			if (data)
			{
				data.setPosition(Math.round((viewportWidth-modal.outerWidth())/2), Math.round((viewportHeight-modal.outerHeight())/2), animate);
			}
		});

		return this;
	};

	/**
	 * Set the modal postion in screen, and make sure the window does not go out of the viewport
	 * @param int x the horizontal position
	 * @param int y the vertical position
	 * @param boolean animate true to animate the window movement
	 */
	$.fn.setModalPosition = function(x, y, animate)
	{
		this.each(function()
		{
			var modal = $(this).getModalWindow(),
				data = (modal.length > 0) ? modal.data('modal') : false;

			// If valid
			if (data)
			{
				data.setPosition(x, y, animate);
			}
		});

		return this;
	};

	/**
	 * Put modal on front
	 */
	$.fn.putModalOnFront = function()
	{
		if ($.modal.all.length > 1)
		{
			var root = getModalRoot();
			this.each(function()
			{
				var modal = $(this).getModalWindow();
				if (modal.next('.modal').length > 0)
				{
					modal.detach().appendTo(root);
				}
			});
		}

		return this;
	};

	/**
	 * Closes the window
	 */
	$.fn.closeModal = function()
	{
		return this.each(function()
		{
			var modal = $(this).getModalWindow(),
				data = modal.data('modal');

			// If valid
			if (data)
			{
				data.close();
			}
		});
	};

	/**
	 * Default modal window options
	 */
	$.modal.defaults = {
		/**
		 * Add a blocking layer to prevent interaction with background content
		 * @var boolean
		 */
		blocker: true,

		/**
		 * Color the blocking layer (translucid black)
		 * @var boolean
		 */
		blockerVisible: true,

		/**
		 * HTML before the content
		 * @var string
		 */
		beforeContent: '',

		/**
		 * HTML after the content
		 * @var string
		 */
		afterContent: '',

		/**
		 * Content of the window: HTML or jQuery object
		 * @var string|jQuery|boolean
		 */
		content: false,

		/**
		 * Add a white background behind content
		 * @var boolean
		 */
		contentBg: true,

		/**
		 * Alignement of contents ('left', 'center' or 'right') ignored for iframes
		 * @var string
		 */
		contentAlign: 'left',

		/**
		 * Uses an iframe for content instead of a div
		 * @var boolean
		 */
		useIframe: false,

		/**
		 * Url for loading content or iframe src
		 * @var string|boolean
		 */
		url: false,

		/**
		 * Options for ajax loading
		 * @var objects
		 */
		ajax: {

			/**
			 * Any message to display while loading, or leave empty to keep current content
			 * @var string|jQuery
			 */
			loadingMessage: null,

			/**
			 * The message to display if a loading error happened. May be a function: function(jqXHR, textStatus, errorThrown)
			 * Ignored if error callback is set
			 * @var string|jQuery
			 */
			errorMessage: 'Error while loading content. Please try again.',

			/**
			 * Use true to resize window on loading message and when content is loaded. To define separately, use options below:
			 * @var boolean
			 */
			resize: false,

			/**
			 * Use true to resize window on loading message
			 * @var boolean
			 */
			resizeOnMessage: false,

			/**
			 * Use true to resize window when content is loaded
			 * @var boolean
			 */
			resizeOnLoad: false

		},

		/**
		 * Show the title bar (use null to auto-detect when title is not empty)
		 * @var boolean|null
		 */
		titleBar: null,

		/**
		 * Title of the window, or false for none
		 * @var string|boolean
		 */
		title: false,

		/**
		 * Enable window moving
		 * @var boolean
		 */
		draggable: true,

		/**
		 * Enable window resizing
		 * @var boolean
		 */
		resizable: true,

		/**
		 * If  true, enable content vertical scrollbar if content is higher than 'height' (or 'maxHeight' if 'height' is undefined)
		 * @var boolean
		 */
		scrolling: true,

		/**
		 * Actions leds on top left corner, with text as key and function on click or config object as value
		 * Ex:
		 *
		 *  {
		 * 		'Close' : function(modal) { modal.closeModal(); }
		 *  }
		 *
		 * Or:
		 *
		 * 	{
		 *  	'Close' : {
		 *  		color :		'red',
		 *  		click :		function(modal) { modal.closeModal(); }
		 *  	}
		 *  }
		 * @var boolean
		 */
		actions: {
			'Close' : {
				color: 'red',
				click: function(modal) { modal.closeModal(); }
			}
		},

		/**
		 * Configuration for action tooltips
		 * @var object
		 */
		actionsTooltips: {
			spacing: 5,
			classes: ['black-gradient'],
			animateMove: 5
		},

		/**
		 * Map of bottom buttons, with text as key and function on click or config object as value
		 * Ex:
		 *
		 *  {
		 * 		'Close' : function(modal) { modal.closeModal(); }
		 *  }
		 *
		 * Or:
		 *
		 * 	{
		 *  	'Close' : {
		 *  		classes :	'blue-gradient glossy huge full-width',
		 *  		click :		function(modal) { modal.closeModal(); }
		 *  	}
		 *  }
		 * @var object
		 */
		buttons: {
			'Close': {
				classes :	'blue-gradient glossy big full-width',
				click :		function(modal) { modal.closeModal(); }
			}
		},

		/**
		 * Alignement of buttons ('left', 'center' or 'right')
		 * @var string
		 */
		buttonsAlign: 'right',

		/**
		 * Use low padding for buttons block
		 * @var boolean
		 */
		buttonsLowPadding: false,

		/**
		 * Function called when opening window
		 * Scope: the modal window
		 * @var function
		 */
		onOpen: false,

		/**
		 * Function called when closing window.
		 * Note: the function may return false to prevent close.
		 * Scope: the modal window
		 * @var function
		 */
		onClose: false,

		/**
		 * Minimum margin to viewport border around window when the max-size is reached
		 * @var int
		 */
		maxSizeMargin: 10,

		/**
		 * Minimum content height
		 * @var int
		 */
		minHeight: 16,

		/**
		 * Minimum content width
		 * @var int
		 */
		minWidth: 200,

		/**
		 * Maximum content width, or false for no limit
		 * @var int|boolean
		 */
		maxHeight: false,

		/**
		 * Maximum content height, or false for no limit
		 * @var int|boolean
		 */
		maxWidth: false,

		/**
		 * Initial content height, or false for fluid size
		 * @var int|boolean
		 */
		height: false,

		/**
		 * Initial content width, or false for fluid size
		 * @var int|boolean
		 */
		width: false,

		/**
		 * Default options for alert() method
		 * @var object
		 */
		alertOptions: {
			contentBg:		false,
			contentAlign:	'center',
			minWidth:		120,
			width:			false,
			maxWidth:		260,
			resizable:		false,
			actions:		{},
			buttons:		{

				'Close' : {
					classes :	'blue-gradient glossy big full-width',
					click :		function(modal) { modal.closeModal(); }
				}

			},
			buttonsAlign:	'center',
			buttonsLowPadding: true
		},

		/**
		 * Default options for prompt() method
		 * @var object
		 */
		promptOptions: {
			width:			false,
			maxWidth:		260,
			resizable:		false,
			actions:		{}
		},

		/**
		 * Default options for confirm() method
		 * @var object
		 */
		confirmOptions: {
			contentAlign:	'center',
			minWidth:		120,
			width:			false,
			maxWidth:		260,
			buttonsAlign:	'center'
		}
	};

})(jQuery, window, document);
/**
 *
 * '||''|.                            '||
 *  ||   ||    ....  .... ...   ....   ||    ...   ... ...  ... ..
 *  ||    || .|...||  '|.  |  .|...||  ||  .|  '|.  ||'  ||  ||' ''
 *  ||    || ||        '|.|   ||       ||  ||   ||  ||    |  ||
 * .||...|'   '|...'    '|     '|...' .||.  '|..|'  ||...'  .||.
 *                                                  ||
 * --------------- By Display:inline ------------- '''' -----------
 *
 * Collapsible menus plugin
 *
 * Structural good practices from the article from Addy Osmani 'Essential jQuery plugin patterns'
 * @url http://coding.smashingmagazine.com/2011/10/11/essential-jquery-plugin-patterns/
 */

/*
 * The semi-colon before the function invocation is a safety
 * net against concatenated scripts and/or other plugins
 * that are not closed properly.
 */
;(function($, window, document)
{
	/*
	 * window and document are passed through as local variables rather than as globals, because this (slightly)
	 * quickens the resolution process and can be more efficiently minified.
	 */

		// Objects cache
	var doc = $(document),

		// Global animation switch
		animate = true;

	// Navigable menus
	doc.on('click', '.collapsible li, .collapsible li > span, .collapsible li > a', function(event)
	{
		// Only work if the element is the event's target
		if (event.target !== this)
		{
			return;
		}

			// Clicked element
		var clicked = $(this),

			// LI element
			li = $(this).closest('li'),

			// Sub-menu
			submenu = li.children('ul:first'),

			// Root menu
			root = li.closest('.collapsible'),

			// Other vars
			load, current, url, height;

		// If there is a submenu
		if (submenu.length > 0)
		{
			// If already open
			if (li.hasClass('collapsible-open'))
			{
				// Close
				li.removeClass('collapsible-open');
				if (animate)
				{
					submenu.stop(true).css('overflow', 'hidden').animate({

						height: '0px'

					}, function()
					{
						submenu.css({
							overflow: '',
							height: ''
						}).hide();
					});
				}
				else
				{
					submenu.stop(true).hide();
				}

				// Arrow
				if (li.hasClass('with-left-arrow') || li.hasClass('with-right-arrow'))
				{
					li.removeClass('arrow-up').addClass('arrow-down');
				}

				// Close event
				li.trigger('collapsible-close');
			}
			else
			{
				// Open parents if required
				$(li.parentsUntil('.collapsible', 'li').not('.collapsible-open').get().reverse()).click();

				// If only one menu should be open on each level
				if (root.hasClass('as-accordion'))
				{
					li.siblings('.collapsible-open').click();
				}

				// Mark as open
				li.addClass('collapsible-open');
				if (animate)
				{
					// Get final size
					height = submenu.stop(true).css({

						display: 'block',
						height: ''

					}).height();

					// Animate
					submenu.css({

						overflow: 'hidden',
						height: '0px'

					}).animate({

						height: height+'px'

					}, function()
					{
						submenu.css({
							overflow: '',
							height: ''
						});
					});
				}
				else
				{
					submenu.stop(true).css({
						overflow: '',
						height: ''
					}).show();
				}

				// Arrow
				if (li.hasClass('with-left-arrow') || li.hasClass('with-right-arrow'))
				{
					li.removeClass('arrow-down').addClass('arrow-up');
				}

				// Open event
				li.trigger('collapsible-open');
			}

			// Prevent default behavior
			event.preventDefault();
		}
		else if (clicked.hasClass('collapsible-ajax'))
		{
			// If already loading, do nothing
			if (li.children('.load').length)
			{
				return;
			}

			// Get target url
			url = clicked.is('a') ? clicked.attr('href') : clicked.data('collapsible-url');

			// If valid
			if (url && typeof url === 'string' && $.trim(url).length > 0 && url.substr(0, 1) !== '#')
			{
				// Load indicator
				load = $('<div class="load"></div>').appendTo(li);

				// Show load
				if (animate)
				{
					height = load.height();
					load.css({

						overflow: 'hidden',
						height: '0px'

					}).animate({

						height: height+'px'

					}, function()
					{
						submenu.css({
							overflow: '',
							height: ''
						});
					});
				}

				// Load submenu
				$.ajax(url, {
					error: function(jqXHR, textStatus, errorThrown)
					{
						// If notification system is enabled
						if (window.notify)
						{
							window.notify('Menu loading failed with the status "'+textStatus+'"');
						}

						// Remove load
						if (animate)
						{
							load.stop(true).css({

								overflow: 'hidden'

							}).animate({

								height: '0px'

							}, function()
							{
								load.remove();
							});
						}
						else
						{
							load.remove();
						}
					},
					success: function(data, textStatus, jqXHR)
					{
						// Remove ajax marker, mark as loaded
						clicked.removeClass('collapsible-ajax').addClass('collapsible-ajax-loaded');

						// Append data
						li.append(data);

						// Finally open the clicked element
						clicked.click();

						// Remove load
						if (animate)
						{
							load.stop(true).css({

								overflow: 'hidden'

							}).animate({

								height: '0px'

							}, function()
							{
								load.remove();
							});
						}
						else
						{
							load.remove();
						}
					}
				});

				// Prevent default behavior
				event.preventDefault();
			}
		}
		else if (clicked.hasClass('collapsible-ajax-loaded'))
		{
			// Probably an ajax menu who loaded nothing, prevent default behavior
			event.preventDefault();
		}
	});

	// Add to template setup function
	$.template.addSetupFunction(function(self, children)
	{
		// Style arrows
		this.findIn(self, children, '.collapsible li').addClass('arrow-down');

		// Current open menu element
		this.findIn(self, children, '.collapsible-current').each(function(i)
		{
			var closest = $(this).closest('ul').closest('li, .collapsible'),
				child;

			// Check if in a submenu
			if (closest.length > 0 && !closest.hasClass('collapsible'))
			{
				// Disable animation
				animate = false;

				// Is there a span or a link?
				child = closest.children('a, span').first();
				if (child.length > 0)
				{
					child.click();
				}
				else
				{
					closest.click();
				}

				// Enable animation
				animate = true;
			}
		})

		return this;
	});

})(jQuery, window, document);
/**
 *
 * '||''|.                            '||
 *  ||   ||    ....  .... ...   ....   ||    ...   ... ...  ... ..
 *  ||    || .|...||  '|.  |  .|...||  ||  .|  '|.  ||'  ||  ||' ''
 *  ||    || ||        '|.|   ||       ||  ||   ||  ||    |  ||
 * .||...|'   '|...'    '|     '|...' .||.  '|..|'  ||...'  .||.
 *                                                  ||
 * --------------- By Display:inline ------------- '''' -----------
 *
 * Notification plugin
 *
 * Structural good practices from the article from Addy Osmani 'Essential jQuery plugin patterns'
 * @url http://coding.smashingmagazine.com/2011/10/11/essential-jquery-plugin-patterns/
 */

/*
 * The semi-colon before the function invocation is a safety
 * net against concatenated scripts and/or other plugins
 * that are not closed properly.
 */
;(function($, window, document, undefined)
{
	/*
	 * undefined is used here as the undefined global variable in ECMAScript 3 is mutable (i.e. it can
	 * be changed by someone else). undefined isn't really being passed in so we can ensure that its value is
	 * truly undefined. In ES5, undefined can no longer be modified.
	 */

	/*
	 * window and document are passed through as local variables rather than as globals, because this (slightly)
	 * quickens the resolution process and can be more efficiently minified.
	 */

	// Objects cache
	var doc = $(document);

	/**
	 * Internal reference: the div holding top notifications
	 * @var jQuery
	 */
	var _topNotificationDiv = false;

	/**
	 * Internal function: retrieve the div holding top notifications
	 *
	 * @return jQuery the div selection
	 */
	function _getTopNotificationDiv()
	{
		if (!_topNotificationDiv)
		{
			_topNotificationDiv = $('<div id="top-notifications"></div>').appendTo(document.body);
		}

		return _topNotificationDiv;
	};

	/**
	 * Internal reference: the div holding bottom notifications
	 * @var jQuery
	 */
	var _bottomNotificationDiv = false;

	/**
	 * Internal function: retrieve the div holding bottom notifications
	 *
	 * @return jQuery the div selection
	 */
	function _getBottomNotificationDiv()
	{
		if (!_bottomNotificationDiv)
		{
			_bottomNotificationDiv = $('<div id="bottom-notifications"></div>').appendTo(document.body);
		}

		return _bottomNotificationDiv;
	};

	/**
	 * Internal function: output archived notifications
	 *
	 * @param jQuery element the archive element
	 * @param int max the max number of output messages
	 * @param object settings the notify options
	 * @return boolean true if there are some more notifications, else false
	 */
	function _releaseNotices(element, max, settings)
	{
		var archive = element.data('notifications-archive') || [],
			releaseCount = (max > 0) ? Math.min(archive.length, max) : archive.length,
			i, archived;

		// Release messages
		for (i = 0; i < releaseCount; ++i)
		{
			archived = archive.shift();
			window.notify(archived.title, archived.message, $.extend(archived.options, { delay: i*100 }), true);
		}

		// If archive is now empty, clear waiting message block
		if (archive.length === 0)
		{
			element.remove();
			return false;
		}
		else
		{
			// Update number of waiting messages
			archiveSize = archive.length;
			element.text((archiveSize > 1) ? settings.textSeveralMore.replace('%d', archiveSize) : settings.textOneMore);
			return true;
		}
	};

	/**
	 * Internal function: activate notification auto close
	 *
	 * @param jQuery element the notification element
	 * @param int delay delay before the notification closes
	 * @return void
	 */
	function _activateNoticeAutoClose(element, delay)
	{
		// Timer
		element.data('notification-timeout', setTimeout(function() { element.fadeAndRemove().trigger('close'); }, delay));

		// Prevent closing when hover
		element.hover(function()
		{
			clearTimeout(element.data('notification-timeout'));

		}, function()
		{
			element.data('notification-timeout', setTimeout(function() { element.fadeAndRemove().trigger('close'); }, delay));
		});
	};

	/**
	 * Display a notification. If the page is not yet ready, delay the notification until it is ready.
	 *
	 * @var string title the title - can be omitted
	 * @var string message a text or html message to display
	 * @var object options an object with any options for the message - optional (see defaults for more details)
	 * @var boolean rotate force deletion of older element before inserting new one - optional (internal mostly, but can be used if relevant)
	 * @return void
	 */
	window.notify = function(title, message, options, rotate)
	{
		// Parameters
		if (message == undefined || typeof message === 'object')
		{
			options = message || {};
			message = title;
			title = '';
		}

		// Defaults
		options = options || {};
		rotate = rotate || false;

		// If the document is not ready or we want some delay
		if (!$.isReady || (options != undefined && options.delay > 0))
		{
			// Delay action
			var delay = (options != undefined) ? (options.delay || 40) : 40;
			setTimeout(function() { window.notify(title, message, $.extend(options, { delay: 0 }), rotate); }, delay);
		}
		else
		{
			// Position defaults
			if (!options.vPos)
			{
				options.vPos = window.notify.defaults.vPos;
			}
			if (!options.hPos)
			{
				options.hPos = window.notify.defaults.hPos;
			}

			var settings = $.extend({},
							// Global defaults
							window.notify.defaults,
							// Defaults for vertical position
							window.notify.defaults[options.vPos.toLowerCase()],
							// Defaults for horizontal position
							window.notify.defaults[options.hPos.toLowerCase()],
							// Defaults for final position
							window.notify.defaults[options.vPos.toLowerCase()+options.hPos.toLowerCase()],
							// User options
							options);

			// System notification
			if (settings.system && window.notify.hasNotificationPermission())
			{
				var notifTitle = ($.trim(title).length > 0) ? title : document.title,
					notification = window.webkitNotifications.createNotification(settings.icon || '', notifTitle, message);

				// Display event
				if (settings.autoClose || settings.onDisplay)
				{
					notification.ondisplay = function()
					{
						// Callback
						if (settings.onDisplay)
						{
							settings.onDisplay.call(notification);
						}

						// Auto-close after delay
						if (settings.autoClose)
						{
							setTimeout(function () { notification.cancel(); }, settings.closeDelay);
						}
					};
				}

				// Click event
				if ((settings.link && settings.link.length > 0) || settings.onClick)
				{
					notification.onclick = function()
					{
						// Callback
						if (settings.onClick)
						{
							settings.onClick.call(notification);
						}

						// Redirection
						if (settings.link && settings.link.length > 0)
						{
							notification.cancel();
							document.location.href = settings.link;
						}
					};
				}

				// Close event
				if (settings.onClose)
				{
					notification.onclose = function()
					{
						// Callback
						settings.onClose.call(notification);
					};
				}

				// Error handling
				notification.onerror = function()
				{
					// If a callback is provided
					if (settings.onError)
					{
						if (settings.onError.call(notification) === false)
						{
							return;
						}
					}

					// Fallback on standard notification
					window.notify(title, message, $.extend(settings, { system: false }), rotate);
				};

				// Show notification
				notification.show();

				// Done for now
				return;
			}

			var classes = ['notification'].concat(settings.classes),
				listId = 'notifications-'+settings.vPos.toLowerCase()+'-'+settings.hPos.toLowerCase(),
				list = $('#'+listId),
				icon = (settings.icon && settings.icon.length > 0) ? '<img class="notification-icon'+(settings.iconOutside ? ' outside' : '')+'" src="'+settings.icon+'">' : '',
				iconArrowSide = (settings.hPos.toLowerCase() === 'left') ? 'right' : 'left',
				iconArrow = (icon.length > 0 && settings.iconOutside) ? '<span class="block-arrow '+iconArrowSide+'"><span></span></span>' : '',
				closeButton = settings.closeButton ? '<span class="close'+(settings.showCloseOnHover ? ' show-on-parent-hover' : '')+'">✕</span>' : '',
				elementTitle = ($.trim(title).length > 0) ? '<h3>'+title+'</h3>' : '',
				altTitle = (settings.title.length > 0) ? ' title="'+settings.title+'"' :'',
				wrapperOpen = (settings.link.length > 0) ? '<a href="'+settings.link+'"'+altTitle+'>' :'<div'+altTitle+'>',
				wrapperClose = (settings.link.length > 0) ? '</a>' :'</div>',
				postponed = false,
				more = list.children('.more-notifications'),
				siblings, block, element, effectMargins;

			// Target list
			if (list.length === 0)
			{
				// Create list
				list = $('<ul id="'+listId+'"></ul>').appendTo((settings.vPos.toLowerCase() === 'top') ? _getTopNotificationDiv() : _getBottomNotificationDiv());
			}

			// List of current messages
			siblings = list.children().not('.closing').not('.more-notifications');

			// If grouping similar messages, check if another one is still displayed
			if (settings.groupSimilar)
			{
				siblings.each(function(i)
				{
					var previous = $(this),
						data = previous.data('notification-data'),
						extras, timeout, archive, archiveSize;

					// Compare messages
					if (data.title === title.toLowerCase() && data.message === message.toLowerCase())
					{
						// Get or create the extra messages block
						extras = previous.children('.extra-notifications');
						if (extras.length > 0)
						{
							// Retrieve existing archive
							archive = extras.data('notifications-archive') || [];
						}
						else
						{
							// Create element and archive
							extras = $('<p class="extra-notifications"></p>').appendTo(previous);
							archive = [];
						}

						// Stop autoClose
						timeout = previous.data('notification-timeout');
						if (timeout)
						{
							clearTimeout(timeout);
							previous.off('mouseenter');
							previous.off('mouseleave');
						}

						// Re-apply if required
						if (!settings.stickGrouped && (timeout || settings.autoClose))
						{
							_activateNoticeAutoClose(previous, settings.closeDelay);
						}

						// Add message to queue
						archive.push({
							title: title,
							message: message,
							options: options
						});

						// Number of waiting messages
						archiveSize = archive.length;
						extras.text((archiveSize > 1) ? settings.textSeveralSimilars.replace('%d', archiveSize) : settings.textOneSimilar);

						// Save archive
						extras.data('notifications-archive', archive);

						// Effect
						if (!previous.is(':animated'))
						{
							previous.shake((doc.width() >= 768) ? 15 : 5);
						}

						// Done
						postponed = true;
						return false;
					}
				});
			}

			// Check if we exceed the simultaneous messages limit
			if (!postponed && settings.limit > 0 && siblings.length >= settings.limit)
			{
				// If rotation
				if (rotate || settings.rotate)
				{
					// Remove first
					siblings.eq(0).addClass('closing').foldAndRemove();
				}
				else
				{
					// Get or create the waiting messages block
					if (more.length > 0)
					{
						// Retrieve existing archive
						archive = more.data('notifications-archive') || [];
					}
					else
					{
						// Create element and archive
						more = $('<li class="notification more-notifications"></li>').appendTo(list);
						archive = [];

						// Behavior
						more.click(function(event)
						{
							_releaseNotices(more, settings.releaseLimit, settings);
						});
					}

					// Add message to queue
					archive.push({
						title: title,
						message: message,
						options: options
					});

					// Number of waiting messages
					archiveSize = archive.length;
					more.text((archiveSize > 1) ? settings.textSeveralMore.replace('%d', archiveSize) : settings.textOneMore);

					// Save archive
					more.data('notifications-archive', archive);

					// Done
					postponed = true;
				}
			}

			// If put in a waiting list, exit
			if (postponed)
			{
				return;
			}

			// If no title
			if ($.trim(title).length === 0)
			{
				classes.push('no-title');
			}

			// Append message
			element = $('<li class="'+classes.join(' ')+'">'+icon+iconArrow+wrapperOpen+elementTitle+message+wrapperClose+closeButton+'</li>');
			if (more.length > 0)
			{
				element.insertBefore(more).hide().slideDown();
			}
			else
			{
				element.appendTo(list).hide().fadeIn('slow');
			}

			// Display callback
			if (settings.onDisplay)
			{
				settings.onDisplay.call(element[0]);
			}

			// Function on click
			if (settings.onClick)
			{
				element.children(':first').on('click', settings.onClick);
			}

			// Save some data
			element.data('notification-data', {
				title: title.toLowerCase(),
				message: message.toLowerCase(),
				closeDelay: settings.closeDelay
			});

			// Watch close button
			element.on('close', function()
			{
				// Mark as closing to prevent similar messages to be added
				element.addClass('closing');

				// Check if queued messages
				var more = list.children('.more-notifications');
				if (more.length > 0)
				{
					// Change fade effect to folding
					element.stop(true).foldAndRemove();

					// Release next notification
					_releaseNotices(more, 1, settings);
				}
			});
			if (settings.onClose)
			{
				element.on('close', settings.onClose);
			}

			// If closing
			if (settings.autoClose)
			{
				_activateNoticeAutoClose(element, settings.closeDelay);
			}
		}
	};

	/**
	 * Check if the Notification API is available
	 * @return boolean true if available, else false
	 */
	window.notify.hasNotificationAPI = function()
	{
		return !!window.webkitNotifications;
	};

	/**
	 * Check if the Notification API permission is set
	 * @return boolean true if available, else false
	 */
	window.notify.isNotificationPermissionSet = function()
	{
		return (window.notify.hasNotificationAPI() && window.webkitNotifications.checkPermission() != 1); // 1 is PERMISSION_NOT_ALLOWED
	};

	/**
	 * Check if the Notification API permission is granted
	 * @return boolean true if available, else false
	 */
	window.notify.hasNotificationPermission = function()
	{
		return (window.notify.hasNotificationAPI() && window.webkitNotifications.checkPermission() == 0); // 0 is PERMISSION_ALLOWED
	};

	/**
	 * Display a message asking the user to grant permission to use the notification API
	 * Note: require the developr.message lib is required if target is not defined
	 * @param jQuery|string target the element which will be clicked to trigger the notification, or a string for a message that will be created on top of #main
	 * @param function callback a function to be called when the permission is set, granted or not (optional)
	 * @return void
	 */
	window.notify.showNotificationPermission = function(target, callback)
	{
		var message = false;

		// If not available or already granted
		if (!window.notify.hasNotificationAPI())
		{
			return;
		}

		// If no target, create a message
		if (typeof target === 'string')
		{
			if (!$.fn.message)
			{
				return;
			}

			message = $('#main').message(target, {
				node:		'a',
				classes:	['align-center', 'green-gradient'],
				simpler:	true,
				inset:		true
			});
			target = message;
		}

		// Behavior
		target.click(function(event)
		{
			// Only for target element (should no be triggered by the close button)
			if (event.target !== this)
			{
				return;
			}

			event.preventDefault();
			window.webkitNotifications.requestPermission(function()
			{
				// Remove message if needed
				if (message)
				{
					message.fadeAndRemove();
				}

				// User callback
				if (callback && typeof callback === 'function')
				{
					callback();
				}
			});
		});
	};

	/**
	 * Notify function defaults
	 * @var object
	 */
	window.notify.defaults = {
		/**
		 * Use system notification if available, else fallback on standard notifications
		 * @var boolean
		 */
		system: false,

		/**
		 * Vertical position ('top' or 'bottom')
		 * @var string
		 */
		vPos: 'top',

		/**
		 * Horizontal position ('left', 'center' or 'right')
		 * Note: ignored in mobile screens (the notification takes the full screen width)
		 * @var string
		 */
		hPos: 'right',

		/**
		 * Extra classes (colors...)
		 * @var array
		 */
		classes: [],

		/**
		 * Link on the notification
		 * @var string
		 */
		link: '',

		/**
		 * Title on hover
		 * @var string
		 */
		title: '',

		/**
		 * Icon path
		 * @var string
		 */
		icon: '',

		/**
		 * Icon should show out of the notification block? (ignored for mobile layouts)
		 * @var boolean
		 */
		iconOutside: true,

		/**
		 * Add a close button to the notification
		 * @var boolean
		 */
		closeButton: true,

		/**
		 * Show the close button only on hover
		 * @var boolean
		 */
		showCloseOnHover: true,

		/**
		 * Notice will close after (closeDelay) ms
		 * @var boolean
		 */
		autoClose: true,

		/**
		 * Delay before notification closes
		 * @var int
		 */
		closeDelay: 8000,

		/**
		 * Delay before showing the notification
		 * @var int
		 */
		delay: 0,

		/**
		 * Group similar notifications in a stack
		 * @var boolean
		 */
		groupSimilar: true,

		/**
		 * Prevent autoClose on grouped notifications
		 * @var boolean
		 */
		stickGrouped: false,

		/**
		 * Text when one similar notification is found
		 * @var boolean
		 */
		textOneSimilar: 'One similar notification',

		/**
		 * Text when several similar notifications are found
		 * Note: use %d in your string to get the final count
		 * @var boolean
		 */
		textSeveralSimilars: '%d similar notifications',

		/**
		 * Maximum number of notifications displayed at the same time in one stack
		 * Note: use 0 for no limit, but use with caution!
		 * @var int
		 */
		limit: 7,

		/**
		 * Force rotation (remove older messages) when reaching limit
		 * @var boolean
		 */
		rotate: false,

		/**
		 * Text when one similar notification is found
		 * @var boolean
		 */
		textOneMore: 'One more notification',

		/**
		 * Text when several similar notifications are found
		 * Note: use %d in your string to get the final count
		 * @var boolean
		 */
		textSeveralMore: '%d more notifications',

		/**
		 * Number of notifications released when clicking on an similiar/archive block
		 * Note: use 0 for no limit, but use with caution!
		 * @var int
		 */
		releaseLimit: 5,

		/**
		 * Options for top notifications
		 * @var object
		 */
		top: {},

		/**
		 * Options for bottom notifications
		 * @var object
		 */
		bottom: {},

		/**
		 * Options for left notifications
		 * @var object
		 */
		left: {},

		/**
		 * Options for center notifications
		 * @var object
		 */
		center: {},

		/**
		 * Options for right notifications
		 * @var object
		 */
		right: {},

		/**
		 * Options for top left notifications
		 * @var object
		 */
		topleft: {},

		/**
		 * Options for top center notifications
		 * @var object
		 */
		topcenter: {},

		/**
		 * Options for top right notifications
		 * @var object
		 */
		topright: {},

		/**
		 * Options for bottom left notifications
		 * @var object
		 */
		bottomleft: {},

		/**
		 * Options for bottom center notifications
		 * @var object
		 */
		bottomcenter: {},

		/**
		 * Options for bottom right notifications
		 * @var object
		 */
		bottomright: {},

		/**
		 * Callback when the notification is shown
		 * @var function
		 */
		onDisplay: null,

		/**
		 * Callback when the notification is clicked
		 * @var function
		 */
		onClick: null,

		/**
		 * Callback when the notification is closed
		 * @var function
		 */
		onClose: null,

		/**
		 * Callback (if using the Notification API system only) if the notification triggers an error.
		 * By default, the lib will fallback on a standard notification, the callback may return false to prevent this.
		 * @var function
		 */
		onError: null
	};

})(jQuery, window, document);
/**
 *
 * '||''|.                            '||
 *  ||   ||    ....  .... ...   ....   ||    ...   ... ...  ... ..
 *  ||    || .|...||  '|.  |  .|...||  ||  .|  '|.  ||'  ||  ||' ''
 *  ||    || ||        '|.|   ||       ||  ||   ||  ||    |  ||
 * .||...|'   '|...'    '|     '|...' .||.  '|..|'  ||...'  .||.
 *                                                  ||
 * --------------- By Display:inline ------------- '''' -----------
 *
 * Custom scroll plugin
 *
 * Structural good practices from the article from Addy Osmani 'Essential jQuery plugin patterns'
 * @url http://coding.smashingmagazine.com/2011/10/11/essential-jquery-plugin-patterns/
 */

/*
 * The semi-colon before the function invocation is a safety
 * net against concatenated scripts and/or other plugins
 * that are not closed properly.
 */
;(function($, document)
{
	/*
	 * document is passed through as local variable rather than as global, because this (slightly)
	 * quickens the resolution process and can be more efficiently minified.
	 */

	// Objects cache
	var doc = $(document);

	/**
	 * Enable custom scroll bar
	 */
	$.fn.customScroll = function(options)
	{
		var globalSettings = $.extend({}, $.fn.customScroll.defaults, options);

		// For elements already scrolling, refresh
		this.filter('.custom-scroll').refreshCustomScroll();

		// Initial setup for others
		this.not('.custom-scroll').addClass('custom-scroll').each(function(i)
		{
			var element = $(this),

				// CSS position
				cssPos = element.css('position'),

				// Type of node for scrollbars
				scrollbarNodeType = element.is('ul, ol') ? 'li' : 'div',

				// Local settings, if inline options are found
				settings = $.extend({}, globalSettings, element.data('scroll-options')),

				// Work vars init
				scrollingH = (element[0].scrollWidth > element.innerWidth()),
				scrollingV = (element[0].scrollHeight > element.innerHeight()),
				scrollH = element.scrollLeft(),
				scrollV = element.scrollTop(),

				// References
				hscrollbar, hscroller,
				vscrollbar, vscroller,

				// Create and refresh functions
				createH = false, createV = false,
				refreshH = false, refreshV = false,

				// Scrollbars visibility
				hiddenH = false,
				hiddenV = false,

				// Check if scrollbar position was already set once
				init = false;

			// The plugin needs position relative, absolute or fixed
			if (cssPos !== 'relative' && cssPos !== 'absolute' && cssPos !== 'fixed')
			{
				element.css('position', 'relative');
			}

			// Format
			if (typeof settings.padding !== 'object')
			{
				settings.padding = {
					top: settings.padding,
					right: settings.padding,
					bottom: settings.padding,
					left: settings.padding
				};
			}
			settings.padding = $.extend({ top: 0, right: 0, bottom: 0, left: 0 }, settings.padding);

			/*
			 * Horizontal scrolling
			 */
			if (settings.horizontal)
			{
				/**
				 * Create horizontal scrollbar
				 */
				createH = function()
				{
					// Create elements
					hscrollbar = $('<'+scrollbarNodeType+' class="custom-hscrollbar"></'+scrollbarNodeType+'>').appendTo(element);
					hscroller = $('<div></div>').appendTo(hscrollbar);

					// Prevent click events from scrollbar
					hscrollbar.click(function(event)
					{
						event.stopPropagation();
					});

					// Prevent text selection for IE7
					hscroller.on('selectstart', _preventTextSelectionIE);

					// Scroller handling
					hscroller.on('mousedown', function(event)
					{
						// Get initial values
						var mouseX = event.pageX,
							hscrollerLeft = hscroller.parseCSSValue('left');

						// Stop text selection
						event.preventDefault();

						// Watch mouse move
						function watchMouse(event)
						{
							var availableSpace = hscrollbar.width()-hscroller.innerWidth(),
								hscrollerPos = Math.max(0, Math.min(availableSpace, hscrollerLeft+(event.pageX-mouseX)));

							// Scroller new position
							hscrollbar[0].style.display = 'none';
							scrollH = (availableSpace > 0) ? Math.round((hscrollerPos/availableSpace)*(element[0].scrollWidth-element.innerWidth())) : 0;
							hscrollbar[0].style.display = 'block';

							// Move
							if (settings.animate && init)
							{
								// Scroll
								element.stop(true).animate({ scrollLeft: scrollH }, {
									step: function()
									{
										$(this).refreshInnerTrackedElements();
									}
								});
							}
							else
							{
								// Scroll
								element.stop(true).scrollLeft(scrollH).refreshInnerTrackedElements();
							}

							// Update scrollbars
							if (refreshH) refreshH();
							if (refreshV) refreshV();
						};
						doc.on('mousemove', watchMouse);

						// Watch for mouseup
						function endDrag()
						{
							doc.off('mousemove', watchMouse);
							doc.off('mouseup', endDrag);
						};
						doc.on('mouseup', endDrag);
					});
				};

				// Init
				createH();

				/**
				 * Refresh horizontal scrollbar and scroll positions/sizes
				 */
				refreshH = function()
				{
					// If disabled
					if (hiddenH)
					{
						return;
					}

					// If scrollbar was removed by a random script
					if (!hscrollbar[0].parentNode)
					{
						createH();
					}

						// Element height
					var elementWidth = element.width(), elementInnerWidth = element.innerWidth(),

						// Margin if vertical scrollbar is enabled too
						vMargin = (settings.vertical && scrollingV && !hiddenV) ? settings.cornerWidth : 0,

						// Scroolbar width
						width = (settings.usePadding ? elementWidth : elementInnerWidth)-settings.padding.top-settings.padding.bottom-vMargin,

						// Minimum scroller width
						minWidth = (width > settings.minScrollerSize*1.5) ? settings.minScrollerSize : Math.round(width/1.5),

						// Available space for scroller
						available = width-minWidth,

						// Scroller size
						size = Math.round(available*(elementWidth/element[0].scrollWidth))+minWidth,

						// Scroller position
						position = Math.round((width-size)*(scrollH/(element[0].scrollWidth-elementInnerWidth)));

					// Reveal scrollbar (hidden in refresh()
					hscrollbar.show();

					// Set scrollbar style
					hscrollbar.stop(true)[(settings.animate && init) ? 'animate' : 'css']({

						// Position
						top: (element.innerHeight()-(settings.usePadding ? element.parseCSSValue('padding-bottom')+settings.padding.top : settings.padding.bottom)-settings.width+scrollV)+'px',
						left: ((settings.usePadding ? element.parseCSSValue('padding-left')+settings.padding.right : settings.padding.left)+scrollH)+'px',

						// Size
						width: width+'px',
						height: settings.width+'px'

					});

					// Set scroller style
					hscroller.stop(true)[(settings.animate && init) ? 'animate' : 'css']({

						// Position
						left: position+'px',

						// Size
						width: Math.round(size)+'px'

					});
				};
			}

			/*
			 * Vertical scrolling
			 */
			if (settings.vertical)
			{
				/**
				 * Create horizontal scrollbar
				 */
				createV = function()
				{
					// Create elements
					vscrollbar = $('<'+scrollbarNodeType+' class="custom-vscrollbar"></'+scrollbarNodeType+'>').appendTo(element);
					vscroller = $('<div></div>').appendTo(vscrollbar);

					// Prevent click events from scrollbar
					vscrollbar.click(function(event)
					{
						event.stopPropagation();
					});

					// Prevent text selection for IE7
					vscroller.on('selectstart', _preventTextSelectionIE);

					// Scroller handling
					vscroller.on('mousedown', function(event)
					{
						// Get initial values
						var mouseY = event.pageY,
							vscrollerTop = vscroller.parseCSSValue('top');

						// Prevent text selection
						event.preventDefault();

						// Watch mouse move
						function watchMouse(event)
						{
							// Scroller new position
							var availableSpace = vscrollbar.height()-vscroller.innerHeight(),
								vscrollerPos = Math.max(0, Math.min(availableSpace, vscrollerTop+(event.pageY-mouseY)));

							// Scroller new position
							vscrollbar[0].style.display = 'none';
							scrollV = (availableSpace > 0) ? Math.round((vscrollerPos/availableSpace)*(element[0].scrollHeight-element.innerHeight())) : 0;
							vscrollbar[0].style.display = 'block';

							// Move
							if (settings.animate && init)
							{
								// Scroll
								element.stop(true).animate({ scrollTop: scrollV }, {
									step: function()
									{
										$(this).refreshInnerTrackedElements();
									}
								});
							}
							else
							{
								// Scroll
								element.stop(true).scrollTop(scrollV).refreshInnerTrackedElements();
							}

							// Update scrollbars
							if (refreshH) refreshH();
							if (refreshV) refreshV();
						};
						doc.on('mousemove', watchMouse);

						// Watch for mouseup
						function endDrag(event)
						{
							event.preventDefault();

							doc.off('mousemove', watchMouse);
							doc.off('mouseup', endDrag);
						};
						doc.on('mouseup', endDrag);
					});
				};

				// Init
				createV();

				/**
				 * Refresh vertical scrollbar and scroll positions/sizes
				 */
				refreshV = function()
				{
					// If disabled
					if (hiddenV)
					{
						return;
					}

					// If scrollbar was removed by a random script
					if (!vscrollbar[0].parentNode)
					{
						createV();
					}

						// Element height
					var elementHeight = element.height(), elementInnerHeight = element.innerHeight(),

						// Margin if horizontal scrollbar is enabled too
						hMargin = (settings.horizontal && scrollingH && !hiddenH) ? settings.cornerWidth : 0,

						// Scroolbar height
						height = (settings.usePadding ? elementHeight : elementInnerHeight)-settings.padding.top-settings.padding.bottom-hMargin,

						// Minimum scroller height
						minHeight = (height > settings.minScrollerSize*1.5) ? settings.minScrollerSize : Math.round(height/1.5),

						// Available space for scroller
						available = height-minHeight,

						// Scroller size
						size = available*(elementHeight/element[0].scrollHeight)+minHeight,

						// Scroller position
						position = Math.round((height-size)*(scrollV/(element[0].scrollHeight-elementInnerHeight)));

					// Reveal scrollbar (hidden in refresh()
					vscrollbar.show();

					// Set scrollbar style
					vscrollbar.stop(true)[(settings.animate && init) ? 'animate' : 'css']({

						// Position
						top: ((settings.usePadding ? element.parseCSSValue('padding-top')+settings.padding.top : settings.padding.top)+scrollV)+'px',
						left: (element.innerWidth()-(settings.usePadding ? element.parseCSSValue('padding-right')+settings.padding.right : settings.padding.right)-settings.width+scrollH)+'px',

						// Size
						height: height+'px',
						width: settings.width+'px'

					});

					// Set scroller style
					vscroller.stop(true)[(settings.animate && init) ? 'animate' : 'css']({

						// Position
						top: position+'px',

						// Size
						height: Math.round(size)+'px'

					});
				};
			}

			/**
			 * Move function
			 * @param int deltaX move on the horizontal axis
			 * @param int deltaY move on the vertical axis
			 * @param boolean doNotAnimate true to skip animation
			 * @return object an object with two keys reporting effective movement { x:0, y:0 }
			 */
			function move(deltaX, deltaY, doNotAnimate)
			{
				// Store initial values
				var initScrollH = scrollH,
					initScrollV = scrollV;

				// New scroll values
				scrollH = Math.max(0, Math.min(scrollH+deltaX, element[0].scrollWidth-element.innerWidth()));
				scrollV = Math.max(0, Math.min(scrollV-deltaY, element[0].scrollHeight-element.innerHeight()));

				// Move
				if (settings.animate && !doNotAnimate && init)
				{
					// Scroll
					element.stop(true).animate({
						scrollLeft: scrollH,
						scrollTop: scrollV
					}, {
						step: function()
						{
							element.refreshInnerTrackedElements();
						}
					});
				}
				else
				{
					// Scroll
					element.scrollLeft(scrollH)
						   .scrollTop(scrollV)
						   .refreshInnerTrackedElements();
				}

				// Update scrollbars
				if (refreshH && deltaX != 0)
				{
					refreshH();
				}
				if (refreshV && deltaY != 0)
				{
					refreshV();
				}

				// Send report
				return {
					x: scrollH-initScrollH,
					y: scrollV-initScrollV
				};
			};

			/**
			 * Handle mouse wheel
			 * @param int deltaX scroll increment on the horizontal axis
			 * @param int deltaY scroll increment on the vertical axis
			 * @param boolean doNotAnimate true to skip animation
			 * @return object an object with two keys reporting effective movement { x:0, y:0 }
			 */
			// Handle mouse wheel
			function mousewheel(deltaX, deltaY, doNotAnimate)
			{
				/*
				 * Some mouse wheels send really small custom scroll deltas when using a custom driver,
				 * for instance 0.05 instead of 1, so we use a minimum value here to prevent these mouses
				 * to scroll too slow
				 */
				if (deltaX != 0)
				{
					deltaX = (deltaX > 0) ? Math.max(deltaX, settings.minWheelScroll) : Math.min(deltaX, -settings.minWheelScroll);
				}
				if (deltaY != 0)
				{
					deltaY = (deltaY > 0) ? Math.max(deltaY, settings.minWheelScroll) : Math.min(deltaY, -settings.minWheelScroll);
				}

				// Move
				return move(deltaX*settings.speed, deltaY*settings.speed, doNotAnimate);
			};

			// Global refresh function
			function refresh()
			{
				// Hide scrollbars to prevent erroneous values
				if (refreshH)
				{
					hscrollbar.hide();
				}
				if (refreshV)
				{
					vscrollbar.hide();
				}

				// Scrolling status
				scrollingH = (element[0].scrollWidth > element.innerWidth());
				scrollingV = (element[0].scrollHeight > element.innerHeight());

				// Update positions
				scrollH = element.scrollLeft();
				scrollV = element.scrollTop();

				// Horizontal scroll status
				if (refreshH)
				{
					hiddenH = (!scrollingH && settings.autoHide);
					refreshH();
				}

				// Vertical scroll status
				if (refreshV)
				{
					hiddenV = (!scrollingV && settings.autoHide);
					refreshV();
				}
			};

			// Store for further calls
			element.data('custom-scroll', {

				// Configuration
				settings: settings,

				// Objects
				hscrollbar:	function() { return hscrollbar;	},
				hscroller:	function() { return hscroller;	},
				vscrollbar:	function() { return vscrollbar;	},
				vscroller:	function() { return vscroller;	},

				// Functions
				refresh: refresh,
				refreshH: refreshV,
				refreshV: refreshV,
				move: move,
				mousewheel: mousewheel

			});

			// First call
			refresh();

			// Fade effect
			if (settings.showOnHover)
			{
				// Initial hiding
				if (hscrollbar) hscrollbar.css({ opacity: 0 });
				if (vscrollbar) vscrollbar.css({ opacity: 0 });

				// Watch
				element.on('mouseenter', _handleScrolledMouseEnter)
					   .on('mouseleave', _handleScrolledMouseLeave);
			}

			// Mark as inited
			init = true;

		}).on('mousewheel', _handleMouseWheel)
		  .on('scroll sizechange scrollsizechange', _handleScroll)
		  .on('touchstart', _handleTouchScroll);

		return this;
	};

	/**
	 * Remove custom scroll
	 */
	$.fn.removeCustomScroll = function()
	{
		this.filter('.custom-scroll')
			.off('mousewheel', _handleMouseWheel)
			.off('scroll sizechange scrollsizechange', _handleScroll)
		  	.off('touchstart', _handleTouchScroll)
			.off('mouseenter', _handleScrolledMouseEnter)
			.off('mouseleave', _handleScrolledMouseLeave)
			.removeData('scroll-options').removeData('touch-scrolling')
			.removeClass('custom-scroll')
			.children('.custom-hscrollbar, .custom-vscrollbar').remove()
			.scrollLeft(0)
			.scrollTop(0);

		return this;
	};

	/**
	 * Internal function: used to prevent text selection under IE (event distint from 'mousedown')
	 *
	 * @return void
	 */
	function _preventTextSelectionIE(event)
	{
		event.preventDefault();
	}

	/**
	 * Internal function: handle fade in effect on mouse hover
	 *
	 * @return void
	 */
	function _handleScrolledMouseEnter()
	{
		if (object = $(this).data('custom-scroll'))
		{
			if (object.hscrollbar()) object.hscrollbar().animate({ opacity: 1 });
			if (object.vscrollbar()) object.vscrollbar().animate({ opacity: 1 });
		}
	};

	/**
	 * Internal function: handle fade out effect on mouse leave
	 *
	 * @return void
	 */
	function _handleScrolledMouseLeave()
	{
		if (object = $(this).data('custom-scroll'))
		{
			if (object.hscrollbar()) object.hscrollbar().animate({ opacity: 0 });
			if (object.vscrollbar()) object.vscrollbar().animate({ opacity: 0 });
		}
	};

	/**
	 * Internal function: handle mousewheel event
	 *
	 * @param object event the event object
	 * @param float delta the vertical delta (historical)
	 * @param float deltaX the vertical delta
	 * @param float deltaY the horizontal delta
	 * @return void
	 */
	function _handleMouseWheel(event, delta, deltaX, deltaY)
	{
		if (object = $(this).data('custom-scroll'))
		{
			// Send scroll
			var movement = object.mousewheel(deltaX, deltaY);

			// If the element scrolled
			if (movement.x != 0 || movement.y != 0 || !object.settings.continuousWheelScroll)
			{
				// Prevent parents from scrolling
				event.preventDefault();
			}
		}
	};

	/**
	 * Internal function: handle scroll event
	 */
	function _handleScroll(event)
	{
		$(this).refreshCustomScroll();
	};

	/**
	 * Internal function: handle touch scroll
	 */
	function _handleTouchScroll(event)
	{
		// Init
		var element = $(this),
			object = element.data('custom-scroll'),
			posX = event.originalEvent.touches[0].pageX, /* jQuery event normalization does not preserve touch events properties */
			posY = event.originalEvent.touches[0].pageY,
			moveFunc, endFunc, movement;

		// If not already touching
		if (object && !element.data('touch-scrolling'))
		{
			// Handle moves
			moveFunc = function(event)
			{

				// Mark as touching
				element.data('touch-scrolling', true);

				// Movement
				var newX = event.originalEvent.touches[0].pageX,
					newY = event.originalEvent.touches[0].pageY;

				// Scroll
				movement = object.move(posX-newX, newY-posY, true);

				// If the element scrolled
				if (movement.x != 0 || movement.y != 0 || !object.settings.continuousTouchScroll)
				{
					// Prevent parents from scrolling
					event.preventDefault();
				}

				// Store for next move
				posX = newX;
				posY = newY;
			};

			// Handle end of touch event
			endFunc = function(event)
			{
				event.stopPropagation();

				// Stop watching
				element.off('touchmove', moveFunc);
				element.off('touchend touchcancel', endFunc);

				// Clear data
				element.removeData('touch-scrolling');
			};

			// Start watching
			element.on('touchmove', moveFunc);
			element.on('touchend touchcancel', endFunc);
		}
	};

	/**
	 * Tell whether the element has custom scrolling
	 * @return boolean true if scrolling, else false
	 */
	$.fn.hasCustomScroll = function()
	{
		return this.data('custom-scroll') ? true : false;
	};

	/**
	 * Refreshes custom scroll bar position
	 */
	$.fn.refreshCustomScroll = function()
	{
		this.each(function(i)
		{
			var object = $(this).data('custom-scroll');
			if (object)
			{
				object.refresh();
			}
		});

		return this;
	};

	/**
	 * Refreshes custom scroll bar position
	 * @param int deltaX the move on the X axis
	 * @param int deltaY the move on the Y axis
	 * @param boolean doNotAnimate true to skip animation
	 */
	$.fn.moveCustomScroll = function(deltaX, deltaY, doNotAnimate)
	{
		this.each(function(i)
		{
			var object = $(this).data('custom-scroll');
			if (object)
			{
				object.move(deltaX, deltaY, doNotAnimate);
			}
		});

		return this;
	};

	/**
	 * Scroll all custom-scroll parent if required to reveal the element
	 */
	$.fn.scrollToReveal = function()
	{
		this.each(function(i)
		{
			var element = $(this),
				scrollParents = element.parents('.custom-scroll');

			// Check for each scroll parent
			scrollParents.each(function(i)
			{
				var scrollParent = $(this),
					scrollOffset, offset,
					parent, object,
					width, height,
					viewWidth, viewHeight, paddings,
					scrollX = 0, scrollY = 0;

				// Scroll object
				object = scrollParent.data('custom-scroll');
				if (!object)
				{
					return;
				}

				// DOM element
				parent = scrollParent[0];

				// Element position
				offset = element.offset();
				scrollOffset = scrollParent.offset();
				offset.top -= scrollOffset.top+scrollParent.parseCSSValue('border-top-width');
				offset.left -= scrollOffset.left+scrollParent.parseCSSValue('border-left-width');

				// Size
				width = element.outerWidth();
				height = element.outerHeight();

				// Paddings
				paddings = {
					top:	object.settings.usePadding ? scrollParent.parseCSSValue('padding-top') : 0,
					right:	object.settings.usePadding ? scrollParent.parseCSSValue('padding-right') : 0,
					bottom:	object.settings.usePadding ? scrollParent.parseCSSValue('padding-bottom') : 0,
					left:	object.settings.usePadding ? scrollParent.parseCSSValue('padding-left') : 0
				};

				// Visible range
				viewWidth = scrollParent.innerWidth();
				viewHeight = scrollParent.innerHeight();

				// Horizontal scroll
				if (offset.left < paddings.left)
				{
					scrollX = paddings.left-offset.left;
				}
				else if (offset.left+width > viewWidth-paddings.right)
				{
					scrollX = viewWidth-paddings.right-offset.left-width;
				}

				// Vertical scroll
				if (offset.top < paddings.top)
				{
					scrollY = paddings.top-offset.top;
				}
				else if (offset.top+height > viewHeight-paddings.bottom)
				{
					scrollY = viewHeight-paddings.bottom-offset.top-height;
				}

				// If any scroll is required
				if (scrollX !== 0 || scrollY !== 0)
				{
					object.move(scrollX, scrollY);
				}
			});
		});

		return this;
	};

	/**
	 * Custom scroll function defaults
	 * @var object
	 */
	$.fn.customScroll.defaults = {
		/**
		 * Horizontal scrolling
		 * @var boolean
		 */
		horizontal: false,

		/**
		 * Vertical scrolling
		 * @var boolean
		 */
		vertical: true,

		/**
		 * Whether to use or ignore element's padding in the scrollbar position
		 * @var boolean
		 */
		usePadding: false,

		/**
		 * Padding around scrollbar (can be a single value if regular, or an object
		 * with 'top', 'right', 'bottom' and 'left' - unset values will be set to 0)
		 * @var int|object
		 */
		padding: 6,

		/**
		 * Scrollbar's width in pixels
		 * @var int
		 */
		width: 14,

		/**
		 * Size of empty space in the corner of both scrollbars when they are enabled
		 * @var int
		 */
		cornerWidth: 10,

		/**
		 * Scroller minimum size, in pixels (will automatically be resized for scrollbars smaller than this value)
		 * @var int
		 */
		minScrollerSize: 30,

		/**
		 * Minimun wheel scroll increment (prevent mouses with custom driver to scroll too slowly)
		 * @var float
		 */
		minWheelScroll: 0.25,

		/**
		 * Use true to let the parent element scroll when the target can not scroll no more (on mouse wheel)
		 * @var boolean
		 */
		continuousWheelScroll: true,

		/**
		 * Use true to let the parent element scroll when the target can not scroll no more (on touch move)
		 * @var boolean
		 */
		continuousTouchScroll: true,

		/**
		 * Speed: move for each mouse scroll
		 * @var int
		 */
		speed: 48,

		/**
		 * Animate scroll movement
		 * @var boolean
		 */
		animate: false,

		/**
		 * Show scrollbars only on hover
		 * @var boolean
		 */
		showOnHover: true,

		/**
		 * Hide useless scrollbars
		 * @var boolean
		 */
		autoHide: true
	};

	// Add to template setup function
	$.template.addSetupFunction(function(self, children)
	{
		// Custom scroll
		this.findIn(self, children, '.scrollable').customScroll();

		return this;
	});

})(jQuery, document);
/**
 *
 * '||''|.                            '||
 *  ||   ||    ....  .... ...   ....   ||    ...   ... ...  ... ..
 *  ||    || .|...||  '|.  |  .|...||  ||  .|  '|.  ||'  ||  ||' ''
 *  ||    || ||        '|.|   ||       ||  ||   ||  ||    |  ||
 * .||...|'   '|...'    '|     '|...' .||.  '|..|'  ||...'  .||.
 *                                                  ||
 * --------------- By Display:inline ------------- '''' -----------
 *
 * Slider & progress plugin
 *
 * Structural good practices from the article from Addy Osmani 'Essential jQuery plugin patterns'
 * @url http://coding.smashingmagazine.com/2011/10/11/essential-jquery-plugin-patterns/
 */

/*
 * The semi-colon before the function invocation is a safety
 * net against concatenated scripts and/or other plugins
 * that are not closed properly.
 */
;(function($, document, undefined)
{
	/*
	 * undefined is used here as the undefined global variable in ECMAScript 3 is mutable (i.e. it can
	 * be changed by someone else). undefined isn't really being passed in so we can ensure that its value is
	 * truly undefined. In ES5, undefined can no longer be modified.
	 */

	/*
	 * document is passed through as local variable rather than as global, because this (slightly)
	 * quickens the resolution process and can be more efficiently minified.
	 */

	// Objects cache
	var doc = $(document);

	/**
	 * Internal function to parse the marks options
	 * @param mixed value the mark option value
	 * @param boolean|string label true or pattern to automaticaly create label
	 * @param string labelAlign alignement default for label
	 * @param int min the min value
	 * @param int max the max value
	 * @param boolean useExtremes true to create marks at start and end of slider (if relevant)
	 * @param boolean insetExtremes true to set alignement (if not set) to inset marks matching min and max (if relevant)
	 * @param boolean horizontal whether marks are on the horizontal axis or not
	 * @return object the final marks array
	 */
	function _parseMarksOption(value, label, labelAlign, min, max, useExtremes, insetExtremes, horizontal)
	{
		var i, step;

		// Mode
		if (typeof value === 'string')
		{
			value = parseFloat(value, 10);
		}
		if (typeof value === 'number' && value > 0)
		{
			// Create marks
			step = value;
			value = [];
			if (useExtremes)
			{
				for (i = min; i <= max; i+=step)
				{
					value.push(i);
				}
			}
			else
			{
				for (i = min+step; i < max; i+=step)
				{
					value.push(i);
				}
			}
		}

		// Other formats
		if (typeof value !== 'object')
		{
			return [];
		}

		// Format points
		for (i = 0; i < value.length; ++i)
		{
			// Convert to object if needed
			if (typeof value[i] !== 'object')
			{
				value[i] = { value: value[i] };
			}

			// Add label
			if (value[i].label == undefined)
			{
				value[i].label = label ? ((label === true) ? value[i].value : label.replace('[value]', value[i].value)) : '';
			}

			// Add alignement
			if (value[i].align == undefined)
			{
				if (insetExtremes && value[i].value === min)
				{
					value[i].align = horizontal ? 'right' : 'bottom';
				}
				else if (insetExtremes && value[i].value === max)
				{
					value[i].align = horizontal ? 'left' : 'top';
				}
				else
				{
					value[i].align = labelAlign;
				}
			}
		}

		return value;
	};

	/**
	 * Internal function to parse auto-spacing options
	 * @param int|boolean config the provided configuration
	 * @param int|boolean global the global option default
	 * @return the parsed configuration
	 */
	function _parseAutoSpacing(config, global)
	{
		// Inheritance
		if (config === null)
		{
			config = global;
		}

		// Format
		if (typeof config !== 'number' && typeof config !== 'boolean')
		{
			config = parseInt(config, 10);
			if (isNaN(config))
			{
				config = 0;
			}
		}

		return config;
	};

	/**
	 * Build the track for a progress/slider
	 * @param object options the options
	 */
	$.fn.buildTrack = function(options)
	{
		this.each(function()
		{
				// Target
			var track = $(this),

				// Final settings
				settings = $.extend({}, $.fn.buildTrack.defaults, options),

				// List of classes
				classes = [],

				// Is the track horizontal ?
				horizontal = (settings.orientation.toLowerCase() !== 'vertical'),

				// Track size
				size,

				// Extra css styles
				style = {},

				// Marks lists
				innerMarks	= (settings.innerMarks)
								? _parseMarksOption(settings.innerMarks, null, null, settings.min, settings.max, false) : [],
				topMarks	= (settings.topMarks && horizontal)
								? _parseMarksOption(settings.topMarks, settings.topLabel, settings.topLabelAlign, settings.min, settings.max, true, settings.insetExtremes, true) : [],
				rightMarks	= (settings.rightMarks && !horizontal)
								? _parseMarksOption(settings.rightMarks, settings.rightLabel, settings.rightLabelAlign, settings.min, settings.max, true, settings.insetExtremes, false) : [],
				bottomMarks	= (settings.bottomMarks && horizontal)
								? _parseMarksOption(settings.bottomMarks, settings.bottomLabel, settings.bottomLabelAlign, settings.min, settings.max, true, settings.insetExtremes, true) : [],
				leftMarks	= (settings.leftMarks && !horizontal)
								? _parseMarksOption(settings.leftMarks, settings.leftLabel, settings.leftLabelAlign, settings.min, settings.max, true, settings.insetExtremes, false) : [],

				// Marks selections
				iMarks = $(),	// Inner
				tMarks = $(),	// Top
				rMarks = $(),	// Right
				bMarks = $(),	// Bottom
				lMarks = $(),	// Left
				hMarks = $(),	// Horizontal
				vMarks = $(),	// Vertical

				// Infos about marks labels
				topLabels = false,
				rightLabels = false,
				bottomLabels = false,
				leftLabels = false,

				// Auto-spacing config
				autoSpacingTop		= _parseAutoSpacing(settings.autoSpacingTop, settings.autoSpacing),
				autoSpacingRight	= _parseAutoSpacing(settings.autoSpacingRight, settings.autoSpacing),
				autoSpacingBottom	= _parseAutoSpacing(settings.autoSpacingBottom, settings.autoSpacing),
				autoSpacingLeft		= _parseAutoSpacing(settings.autoSpacingLeft, settings.autoSpacing),
				autoSpacing = (autoSpacingTop !== false && autoSpacingRight !== false && autoSpacingBottom !== false && autoSpacingLeft !== false),

				// Work vars
				position, label, labelAlign;

			// Orientation
			if (!horizontal)
			{
				classes.push('vertical');
			}

			// Classes
			if (typeof settings.classes === 'string')
			{
				classes.push(settings.classes);
			}
			else if (typeof settings.classes === 'object')
			{
				classes = classes.concat(settings.classes);
			}

			// Size
			if (settings.size)
			{
				// Unit
				if (typeof settings.size === 'number')
				{
					settings.size += 'px';
				}
				style[horizontal ? 'width' : 'height'] = settings.size;
			}
			else if (horizontal)
			{
				classes.push('full-width');
				autoSpacingLeft = false;
				autoSpacingRight = false;
			}

			// Slider base
			track.addClass(classes.join(' '));
			track.css($.extend(style, settings.css));
			size = horizontal ? track.innerWidth() : track.innerHeight();

			// Watch size for fluid elements
			if (!track[0].style.width || !track[0].style.width.match(/[0-9\.]+(px|em)/i))
			{
				track.sizechange(function()
				{
					// Refresh size cache
					size = horizontal ? track.innerWidth() : track.innerHeight();

					// Marks
					hMarks.each(function(i)
					{
						var element = $(this),
						value = element.data('mark-value');

						element.css('left', Math.round((value-settings.min)/(settings.max-settings.min)*(size-1)+1)+'px');
					});
					vMarks.each(function(i)
					{
						var element = $(this),
						value = element.data('mark-value');

						element.css('bottom', Math.round((value-settings.min)/(settings.max-settings.min)*(size-1)+1)+'px');
					});

					// Listener
					if (track.onSizechange)
					{
						track.onSizechange();
					}

					// Auto spacing
					addSpacings();
				});
			}

			// Create required marks
			for (i = 0; i < innerMarks.length; ++i)
			{
				position = Math.round((innerMarks[i].value-settings.min)/(settings.max-settings.min)*(size-1))+1;
				iMarks = iMarks.add($('<span class="inner-mark" style="'+(horizontal ? 'left' : 'bottom')+': '+position+'px"></span>').appendTo(track).data('mark-value', innerMarks[i].value));
			}
			for (i = 0; i < topMarks.length; ++i)
			{
				position = Math.round((topMarks[i].value-settings.min)/(settings.max-settings.min)*(size-1))+1;
				if (topMarks[i].label !== undefined && topMarks[i].label !== '')
				{
					labelAlign = (topMarks[i].align === 'left' || topMarks[i].align === 'right') ? ' align-'+topMarks[i].align : '';
					label = '<span class="mark-label'+labelAlign+'">'+topMarks[i].label+'</span>';
					topLabels = true;
				}
				else
				{
					label = '';
				}
				tMarks = tMarks.add($('<span class="top-mark" style="left: '+position+'px">'+label+'</span>').appendTo(track).data('mark-value', topMarks[i].value));
			}
			for (i = 0; i < rightMarks.length; ++i)
			{
				position = Math.round((rightMarks[i].value-settings.min)/(settings.max-settings.min)*(size-1))+1;
				if (rightMarks[i].label !== undefined && rightMarks[i].label !== '')
				{
					labelAlign = (rightMarks[i].align === 'top' || rightMarks[i].align === 'bottom') ? ' align-'+rightMarks[i].align : '';
					label = '<span class="mark-label'+labelAlign+'">'+rightMarks[i].label+'</span>';
					rightLabels = true;
				}
				else
				{
					label = '';
				}
				rMarks = rMarks.add($('<span class="right-mark" style="bottom: '+position+'px">'+label+'</span>').appendTo(track).data('mark-value', rightMarks[i].value));
			}
			for (i = 0; i < bottomMarks.length; ++i)
			{
				position = Math.round((bottomMarks[i].value-settings.min)/(settings.max-settings.min)*(size-1))+1;
				if (bottomMarks[i].label !== undefined && bottomMarks[i].label !== '')
				{
					labelAlign = (bottomMarks[i].align === 'left' || bottomMarks[i].align === 'right') ? ' align-'+bottomMarks[i].align : '';
					label = '<span class="mark-label'+labelAlign+'">'+bottomMarks[i].label+'</span>';
					bottomLabels = true;
				}
				else
				{
					label = '';
				}
				bMarks = bMarks.add($('<span class="bottom-mark" style="left: '+position+'px">'+label+'</span>').appendTo(track).data('mark-value', bottomMarks[i].value));
			}
			for (i = 0; i < leftMarks.length; ++i)
			{
				position = Math.round((leftMarks[i].value-settings.min)/(settings.max-settings.min)*(size-1))+1;
				if (leftMarks[i].label !== undefined && leftMarks[i].label !== '')
				{
					labelAlign = (leftMarks[i].align === 'top' || leftMarks[i].align === 'bottom') ? ' align-'+leftMarks[i].align : '';
					label = '<span class="mark-label'+labelAlign+'">'+leftMarks[i].label+'</span>';
					leftLabels = true;
				}
				else
				{
					label = '';
				}
				lMarks = lMarks.add($('<span class="left-mark" style="bottom: '+position+'px">'+label+'</span>').appendTo(track).data('mark-value', leftMarks[i].value));
			}

			// Concat for further processing
			hMarks = tMarks.add(bMarks);
			vMarks = lMarks.add(rMarks);
			if (horizontal)
			{
				hMarks = hMarks.add(iMarks);
			}
			else
			{
				vMarks = vMarks.add(iMarks);
			}

			// Add spacing margins
			function addSpacings()
			{
				// If enabled
				if (autoSpacing)
				{
					// Init
					var sliderOffset = track.offset(),
						bounds = {
							top:		sliderOffset.top,
							right:		sliderOffset.left+track.outerWidth(),
							bottom:		sliderOffset.top+track.outerHeight(),
							left:		sliderOffset.left
						},
						labelBounds = {
							top:		bounds.top,
							right:		bounds.right,
							bottom:		bounds.bottom,
							left:		bounds.left
						},
						enableTop		= (autoSpacingTop !== false),
						enableRight		= (autoSpacingRight !== false),
						enableBottom	= (autoSpacingBottom !== false),
						enableLeft		= (autoSpacingLeft !== false),
						extraTop		= (typeof autoSpacingTop === 'number'		? autoSpacingTop	: 0),
						extraRight		= (typeof autoSpacingRight === 'number'		? autoSpacingRight	: 0),
						extraBottom		= (typeof autoSpacingBottom === 'number'	? autoSpacingBottom	: 0),
						extraLeft		= (typeof autoSpacingLeft === 'number'		? autoSpacingLeft	: 0),
						css;

					// Check marks
					if (horizontal)
					{
						if (topMarks.length > 0)
						{
							// Mark size
							labelBounds.top = enableTop ? bounds.top-8 : bounds.top;
							if (topLabels)
							{
								// Check bounds
								tMarks.children('.mark-label').each(function(i)
								{
									var label = $(this),
										offset = label.offset(),
										width, textWidth,
										left, right;

									// Exact width (text extent inside fixed width element)
									if (enableLeft || enableRight)
									{
										width = label.width();
										if (!label.hasClass('align-left') && !label.hasClass('align-right'))
										{
											label.css('width', 'auto');
											textWidth = Math.min(width, label.width());
											left = offset.left+Math.round((width/2)-(textWidth/2));
											right = offset.left+Math.round((width/2)+(textWidth/2));
											label.css('width', '');
										}
										else
										{
											left = offset.left;
											right = offset.left+width;
										}
									}

									// Bounds
									labelBounds.top		= enableTop		? Math.min(offset.top, labelBounds.top)	: labelBounds.top;
									labelBounds.right	= enableRight	? Math.max(right, labelBounds.right)	: labelBounds.right;
									labelBounds.left	= enableLeft	? Math.min(left, labelBounds.left)		: labelBounds.left;
								});
							}
						}
						if (bottomMarks.length > 0)
						{
							// Mark size
							labelBounds.bottom = enableBottom ? bounds.bottom+8 : bounds.bottom;
							if (bottomLabels)
							{
								// Check bounds
								bMarks.children('.mark-label').each(function(i)
								{
									var label = $(this),
										offset = label.offset(),
										width, textWidth,
										left, right;

									// Exact width (text extent inside fixed width element)
									if (enableLeft || enableRight)
									{
										width = label.width();
										if (!label.hasClass('align-left') && !label.hasClass('align-right'))
										{
											label.css('width', 'auto');
											textWidth = Math.min(width, label.width());
											left = offset.left+Math.round((width/2)-(textWidth/2));
											right = offset.left+Math.round((width/2)+(textWidth/2));
											label.css('width', '');
										}
										else
										{
											left = offset.left;
											right = offset.left+width;
										}
									}

									// Bounds
									labelBounds.right	= enableRight	? Math.max(right, labelBounds.right)						: labelBounds.right;
									labelBounds.bottom	= enableBottom	? Math.max(offset.top+label.height(), labelBounds.bottom)	: labelBounds.bottom;
									labelBounds.left	= enableLeft	? Math.min(left, labelBounds.left)							: labelBounds.left;

									// Reset
									label.css('width', '');
								});
							}
						}
					}
					else
					{
						if (leftMarks.length > 0)
						{
							// Mark size
							labelBounds.left = enableLeft ? bounds.left-8 : bounds.left;
							if (leftLabels)
							{
								// Check bounds
								lMarks.children('.mark-label').each(function(i)
								{
									var label = $(this),
										offset = label.offset();

									// Bounds
									labelBounds.top		= enableTop		? Math.min(offset.top, labelBounds.top)						: labelBounds.top;
									labelBounds.bottom	= enableBottom	? Math.max(offset.top+label.height(), labelBounds.bottom)	: labelBounds.bottom;
									labelBounds.left	= enableLeft	? Math.min(offset.left, labelBounds.left)					: labelBounds.left;
								});
							}
						}
						if (rightMarks.length > 0)
						{
							// Mark size
							labelBounds.right = enableRight ? bounds.right+8 : bounds.right;
							if (rightLabels)
							{
								// Check bounds
								rMarks.children('.mark-label').each(function(i)
								{
									var label = $(this),
										offset = label.offset();

									// Bounds
									labelBounds.top		= enableTop		? Math.min(offset.top, labelBounds.top)						: labelBounds.top;
									labelBounds.right	= enableRight	? Math.max(offset.left+label.width(), labelBounds.right)	: labelBounds.right;
									labelBounds.bottom	= enableBottom	? Math.max(offset.top+label.height(), labelBounds.bottom)	: labelBounds.bottom;
								});
							}
						}
					}

					// Final values
					css = {
						marginTop:		(labelBounds.top < bounds.top)			? ((bounds.top-labelBounds.top)+extraTop)+'px'			: (extraTop > 0 ? extraTop+'px' : ''),
						marginRight:	(labelBounds.right > bounds.right)		? ((labelBounds.right-bounds.right)+extraRight)+'px'	: (extraRight > 0 ? extraRight+'px' : ''),
						marginBottom:	(labelBounds.bottom > bounds.bottom)	? ((labelBounds.bottom-bounds.bottom)+extraBottom)+'px'	: (extraBottom > 0 ? extraBottom+'px' : ''),
						marginLeft:		(labelBounds.left < bounds.left)		? ((bounds.left-labelBounds.left)+extraLeft)+'px'		: (extraLeft > 0 ? extraLeft+'px' : '')
					};

					// Centering
					if (settings.autoSpacingCenterVertical && enableTop && enableBottom && css.marginTop !== css.marginBottom)
					{
						if (css.marginTop === '')
						{
							css.marginTop = css.marginBottom;
						}
						else if (css.marginBottom === '')
						{
							css.marginBottom = css.marginTop;
						}
						else
						{
							css.marginTop = (parseInt(css.marginTop, 10) > parseInt(css.marginBottom, 10)) ? css.marginTop : css.marginBottom;
							css.marginBottom = css.marginTop;
						}
					}
					if (settings.autoSpacingCenterHorizontal && enableLeft && enableRight && css.marginLeft !== css.marginRight)
					{
						if (css.marginLeft === '')
						{
							css.marginLeft = css.marginRight;
						}
						else if (css.marginRight === '')
						{
							css.marginRight = css.marginLeft;
						}
						else
						{
							css.marginLeft = (parseInt(css.marginLeft, 10) > parseInt(css.marginRight, 10)) ? css.marginLeft : css.marginRight;
							css.marginRight = css.marginLeft;
						}
					}

					// Set
					track.css(css);
				}
			}

			// First setup
			addSpacings();
		});

		return this;
	};

	/**
	 * Default track options
	 */
	$.fn.buildTrack.defaults = {

		/**
		 * Width/height of the track (any css value, int will be used as pixels), or false for fluid size
		 * @var int|string|boolean
		 */
		size: 250,

		/**
		 * Class or list of class for the bar
		 * @var string|array
		 */
		classes: null,

		/**
		 * Any extra css styles, in a key/value map
		 * @var object
		 */
		css: {},

		/**
		 * Orientation of the track ('horizontal' or 'vertical')
		 * @var string
		 */
		orientation: 'horizontal',

		/**
		 * Inner marks, may be:
		 * - a number: marks will be created each multiple of this number, starting from 'min'
		 * - an array: a list of points where marks should be created.
		 * @var string|array
		 */
		innerMarks: null,

		/**
		 * Top marks, may be:
		 * - a number: marks will be created each multiple of this number, starting from 'min'
		 * - an array: a list of points where marks should be created. Each point can be either a number or an object
		 * 			   with a value and two optional properties, 'label' and 'align': { value: x, label: 'label', align: 'center' }.
		 * 			   If the point is a number or if 'label' is not set, an automatic label will be created if topLabel is set.
		 * 			   If the point is a number or if 'align' is not set, the value of topLabelAlign will be used.
		 * @var string|array
		 */
		topMarks: null,

		/**
		 * Automatic label for top marks. Use true to display mark value, or a string with [value] as a placeholder
		 * for each mark value, for instance: '[value]%'
		 * @var boolean|string
		 */
		topLabel: null,

		/**
		 * Alignement for top marks labels: 'left', 'center' or 'right'
		 * @var string
		 */
		topLabelAlign: 'center',

		/**
		 * Right marks, may be:
		 * - a number: marks will be created each multiple of this number, starting from 'min'
		 * - an array: a list of points where marks should be created. Each point can be either a number or an object
		 * 			   with a value and an optional properties, 'label': { value: x, label: 'label' }.
		 * 			   If the point is a number or if 'label' is not set, an automatic label will be created if rightLabel is set.
		 * @var string|array
		 */
		rightMarks: null,

		/**
		 * Automatic label for right marks. Use true to display mark value, or a string with [value] as a placeholder
		 * for each mark value, for instance: '[value]%'
		 * @var boolean|string
		 */
		rightLabel: null,

		/**
		 * Alignement for right marks labels: 'top', 'center' or 'bottom'
		 * @var string
		 */
		rightLabelAlign: 'center',

		/**
		 * Bottom marks, may be:
		 * - a number: marks will be created each multiple of this number, starting from 'min'
		 * - an array: a list of points where marks should be created. Each point can be either a number or an object
		 * 			   with a value and two optional properties, 'label' and 'align': { value: x, label: 'label', align: 'center' }.
		 * 			   If the point is a number or if 'label' is not set, an automatic label will be created if bottomLabel is set.
		 * 			   If the point is a number or if 'align' is not set, the value of bottomLabelAlign will be used.
		 * @var string|array
		 */
		bottomMarks: null,

		/**
		 * Automatic label for top marks. Use true to display mark value, or a string with [value] as a placeholder
		 * for each mark value, for instance: '[value]%'
		 * @var boolean|string
		 */
		bottomLabel: null,

		/**
		 * Alignement for bottom marks labels: 'left', 'center' or 'right'
		 * @var string
		 */
		bottomLabelAlign: 'center',

		/**
		 * Left marks, may be:
		 * - a number: marks will be created each multiple of this number, starting from 'min'
		 * - an array: a list of points where marks should be created. Each point can be either a number or an object
		 * 			   with a value and an optional properties, 'label': { value: x, label: 'label' }.
		 * 			   If the point is a number or if 'label' is not set, an automatic label will be created if leftLabel is set.
		 * @var string|array
		 */
		leftMarks: null,

		/**
		 * Automatic label for left marks. Use true to display mark value, or a string with [value] as a placeholder
		 * for each mark value, for instance: '[value]%'
		 * @var boolean|string
		 */
		leftLabel: null,

		/**
		 * Alignement for left marks labels: 'top', 'center' or 'bottom'
		 * @var string
		 */
		leftLabelAlign: 'center',

		/**
		 * Automaticaly set alignement (if not set) to inset the labels of marks matching min and max
		 * @var boolean
		 */
		insetExtremes: false,

		/**
		 * Will add spacing margins to the track, matching space required by marks and labels - true to enable, false to disable.
		 * Setting a numeric value will enabled auto-spacing and add this as an extra margin on top of calculated one.
		 * @var boolean
		 */
		autoSpacing: false,

		/**
		 * Auto top spacing: true to enable, false to disable, null to inherit from autoSpacing.
		 * Setting a numeric value will enable auto-spacing and add this as an extra margin on top of calculated one.
		 * @var boolean|int
		 */
		autoSpacingTop: null,

		/**
		 * Auto right spacing: true to enable, false to disable, null to inherit from autoSpacing.
		 * Setting a numeric value will enable auto-spacing and add this as an extra margin on top of calculated one.
		 * @var boolean|int
		 */
		autoSpacingRight: null,

		/**
		 * Auto bottom spacing: true to enable, false to disable, null to inherit from autoSpacing.
		 * Setting a numeric value will enable auto-spacing and add this as an extra margin on top of calculated one.
		 * @var boolean|int
		 */
		autoSpacingBottom: null,

		/**
		 * Auto left spacing: true to enable, false to disable, null to inherit from autoSpacing.
		 * Setting a numeric value will enable auto-spacing and add this as an extra margin on top of calculated one.
		 * @var boolean|int
		 */
		autoSpacingLeft: null,

		/**
		 * When auto-spacing, equalize top and bottom margins to the highest - for instance to keep alignement on baseline
		 * @var boolean
		 */
		autoSpacingCenterVertical: false,

		/**
		 * When auto-spacing, equalize left and right margins to the highest - for instance to center in a block
		 * @var boolean
		 */
		autoSpacingCenterHorizontal: false

	};

	/**
	 * Internal function for validating options, so we can work on it without worrying about format
	 * @param object options the options object
	 * @return void
	 */
	function _validateOptions(options)
	{
		var list, i;

		// If set
		if (options)
		{
			// Inputs number and types
			if (options.inputs)
			{
				options.inputs = _validateInputs(options.inputs, true);
			}
			if (options.input)
			{
				options.input = _validateInputs(options.input, false);
			}

			// Values number
			if (options.values)
			{
				if (typeof options.values !== 'object' || options.values.length < 2)
				{
					options.values = null;
				}
			}

			// Tooltip position
			if (options.tooltip && typeof options.tooltip === 'object')
			{
				if (options.tooltip.length === 1)
				{
					options.tooltip = [options.tooltip[0], options.tooltip[0]];
				}
				else if (options.tooltip.length === 0)
				{
					options.tooltip = null;
				}
			}
		}

		return options;
	};

	/**
	 * Start watching an input related to a slider
	 * @param int index the index of the input in the cursors list
	 * @param function setValue the value to set slider's value
	 * @param int min the minimum value
	 * @param int max the maximum value
	 */
	function _watchSliderInput()
	{
		var input = $(this),
			last;

		// Watch keydown
		input.on('keydown.slider', function(event)
		{
			// If up and down
			if (event.which === 38 || event.which === 40)
			{
				var value = parseFloat(input.val());

				// Check if numeric
				if (!isNaN(value))
				{
					value += (event.shiftKey) ? ((event.which === 38) ? 10 : -10) : ((event.which === 38) ? 1 : -1);
					input.val(value);
				}
			}
		});

		// Watch keyup
		input.on('keyup.slider', function(event)
		{
			var value = input.val();

			// Only trigger change if the content has changed
			if (value === last)
			{
				return;
			}

			// Update slider
			input.trigger('change');

			// Store for next check
			last = value;
		});
	};

	/**
	 * End watching an input related to a slider
	 */
	function _endWatchSliderInput()
	{
		$(this).off('keydown.slider').off('keyup.slider');
	};

	/**
	 * Validate the inputs option
	 * @param string|array inputs the given option
	 * @param boolean multiple true for 2 inputs, false for one
	 * @return jQuery|array|boolean a jQuery selection if one input is required, else an array with the inputs, or false if invalid
	 */
	function _validateInputs(inputs, multiple)
	{
		var i, list;

		// Empty
		if (!inputs)
		{
			return false;
		}
		else if (typeof inputs === 'string')
		{
			inputs = $(inputs).filter('input');
		}
		else if (typeof inputs === 'object')
		{
			// Array
			if (!(inputs instanceof jQuery))
			{
				// Format array values
				list = inputs;
				inputs = $();
				for (i = 0; i < list.length; ++i)
				{
					// Type
					if (typeof list[i] === 'string')
					{
						list[i] = $(list[i]);
					}

					// Format
					if (list[i] instanceof jQuery)
					{
						inputs = inputs.add(list[i]);
					}
				}
			}

			// Validation
			inputs = inputs.filter('input');
		}
		else
		{
			return false;
		}

		// Number of required inputs
		if (!multiple)
		{
			return (inputs.length > 0) ? inputs.eq(0) : false;
		}
		else
		{
			return (inputs.length > 1) ? [inputs.eq(0), inputs.eq(1)] : false;
		}
	}

	/**
	 * Create a slider in the target element, or next to the target element if it is an input
	 * Options may be set using the inline html5 data-slider-options attribute:
	 * <div data-slider-options="{'max':200}"></div>
	 * @param object options
	 */
	$.fn.slider = function(options)
	{
		// Validation of options
		_validateOptions(options);

		this.each(function()
		{
				// Target
			var target = $(this),

				// Is the target an input ?
				isInput = (this.nodeName.toLowerCase() === 'input'),

				// Slider size
				size,

				// Inline options
				userOptions = $.extend({}, options, _validateOptions(target.data('slider-options'))),

				// Final settings
				settings = $.extend({}, $.fn.slider.defaults, userOptions),

				// Is the slider horizontal ?
				horizontal = (settings.orientation.toLowerCase() !== 'vertical'),

				// List of classes
				barClasses = ['slider-bar'], tooltipClasses = ['message', 'inner-tooltip'],

				// Bar mode
				barModeMin = (settings.barMode.toLowerCase() !== 'max'),

				// Is it a range
				range = (settings.inputs !== null || settings.values !== null),

				// Are there inputs
				inputs = range ? settings.inputs : (isInput ? target : settings.input),

				// Objects
				bar, cursor1, cursor2, cursors,

				// Tell if dragging and hovering
				dragging = false, hovering = false,

				// Do we use tooltips ?
				useTooltips = (settings.tooltip !== null && settings.tooltip !== false),

				// Enable/disabled change event listeners
				useEvents = true,

				// Stripes
				stripesSize, animatedStripes, darkStripes,

				// Work vars
				value, finalValue, init = false, tooltip;

			// Prepare inputs
			if (inputs)
			{
				if (range)
				{
					// Hide if required
					if (settings.hideInput)
					{
						inputs[0].hide();
						inputs[1].hide();
					}
					else
					{
						if (inputs[0].prop('type').toLowerCase() !== 'hidden')
						{
							inputs[0].focus(function()
							{
								_watchSliderInput.call(this, 0, setValue, settings.min, settings.max);

							}).blur(function()
							{
								_endWatchSliderInput.call(this);
								inputs[0].val(value[0]);
							});
						}
						if (inputs[1].prop('type').toLowerCase() !== 'hidden')
						{
							inputs[1].focus(function()
							{
								_watchSliderInput.call(this, 1, setValue, settings.min, settings.max);

							}).blur(function()
							{
								_endWatchSliderInput.call(this);
								inputs[1].val(value[1]);
							});
						}
					}

					// Watch changes
					inputs[0].change(function(event)
					{
						if (useEvents)
						{
							useEvents = false;
							setValue(inputs[0].val(), null);
							useEvents = true;
						}
					});
					inputs[1].change(function(event)
					{
						if (useEvents)
						{
							useEvents = false;
							setValue(null, $(this).val());
							useEvents = true;
						}
					});
				}
				else
				{
					// Hide if required
					if (settings.hideInput)
					{
						inputs.hide();
					}
					else if (inputs.prop('type').toLowerCase() !== 'hidden')
					{
						inputs.focus(function()
						{
							_watchSliderInput.call(this, 0, setValue, settings.min, settings.max);

						}).blur(function()
						{
							_endWatchSliderInput.call(this);
							inputs.val(value);
						});
					}

					// Watch changes
					inputs.change(function(event)
					{
						if (useEvents)
						{
							useEvents = false;
							setValue($(this).val());
							useEvents = true;
						}
					});
				}
			}

			// Retrieve and normalize values
			if (range)
			{
				if (inputs && !userOptions.values)
				{
					value = [
						normalizeValue(inputs[0].val(), settings.min),
						normalizeValue(inputs[1].val(), settings.max)
					];
				}
				else
				{
					value = [
						normalizeValue(settings.values[0], settings.min),
						normalizeValue(settings.values[1], settings.max)
					];
				}

				// Final values
				finalValue = [
					finalizeValue(value[0]),
					finalizeValue(value[1])
				];
			}
			else
			{
				if (inputs && !userOptions.values)
				{
					value = normalizeValue(inputs.val(), settings.min);
				}
				else
				{
					value = normalizeValue(settings.value, settings.min);
				}

				// Final values
				finalValue = finalizeValue(value);
			}

			// Slider base
			if (isInput)
			{
				target = $('<span></span>').insertAfter(target);
			}
			target.addClass('slider').buildTrack(settings);
			size = horizontal ? target.innerWidth() : target.innerHeight();

			// Bar
			if (typeof settings.barClasses === 'string')
			{
				barClasses.push(settings.barClasses);
			}
			else if (typeof settings.barClasses === 'object')
			{
				barClasses = barClasses.concat(settings.barClasses);
			}
			bar = $('<span class="'+barClasses.join(' ')+'"></span>');
			bar[settings.innerMarksOverBar ? 'prependTo' : 'appendTo'](target);

			// Stripes
			if (settings.stripes)
			{
				// Dark or not
				darkStripes = settings.darkStripes ? 'dark-' : '';

				// Size
				stripesSize = (settings.stripesSize === 'big' || settings.stripesSize === 'thin') ? settings.stripesSize+'-' : '';

				// Animated
				animatedStripes = settings.animatedStripes ? ' animated' : '';

				// Final
				bar.append('<span class="'+darkStripes+stripesSize+'stripes'+animatedStripes+'"></span>');
			}

			// Build cursor tooltip
			function buildTooltip(position)
			{
				// Mode auto
				if (!$.inArray(position, ['top', 'right', 'bottom', 'left']))
				{
					position = horizontal ? 'top' : 'right';
				}

				switch (position)
				{
					case 'right':
						return '<span class="'+tooltipClasses.join(' ')+' right"><span class="tooltip-value"></span><span class="block-arrow left"><span></span></span></span>';
						break;

					case 'bottom':
						return '<span class="'+tooltipClasses.join(' ')+' bottom"><span class="tooltip-value"></span><span class="block-arrow top"><span></span></span></span>';
						break;

					case 'left':
						return '<span class="'+tooltipClasses.join(' ')+' left"><span class="tooltip-value"></span><span class="block-arrow right"><span></span></span></span>';
						break;

					default:
						return '<span class="'+tooltipClasses.join(' ')+'"><span class="tooltip-value"></span><span class="block-arrow"><span></span></span></span>';
						break;
				}
			}

			// Tooltip
			tooltip = '';
			if (useTooltips)
			{
				// User classes
				if (typeof settings.tooltipClass === 'string')
				{
					tooltipClasses.push(settings.tooltipClass);
				}
				else if (typeof settings.tooltipClass === 'object')
				{
					tooltipClasses = tooltipClasses.concat(settings.tooltipClass);
				}

				// Position format
				if (typeof settings.tooltip !== 'object')
				{
					settings.tooltip = [settings.tooltip, settings.tooltip];
				}

				// Code
				tooltip = buildTooltip(settings.tooltip[0]);
			}

			// Cursors
			cursor1 = $('<span class="slider-cursor'+(settings.knob ? ' knob' : '')+'">'+tooltip+'</span>').appendTo(target);
			cursors = cursor1;
			if (range)
			{
				// Tooltip
				if (useTooltips && settings.tooltip[0] !== settings.tooltip[1])
				{
					tooltip = buildTooltip(settings.tooltip[1]);
				}

				// Create
				cursor1.data('slider-range-index', 0);
				cursor2 = $('<span class="slider-cursor'+(settings.knob ? ' knob' : '')+'">'+tooltip+'</span>').appendTo(target).data('slider-range-index', 1);
				cursors = cursors.add(cursor2);
			}

			// Behaviour
			if (useTooltips && settings.tooltipOnHover)
			{
				if (!Modernizr.touch)
				{
					target.hover(function(event)
					{
						if (!dragging)
						{
							cursors.children('.inner-tooltip').stop(true).fadeTo('fast', 1);
						}
						hovering = true;

					}, function(event)
					{
						if (!dragging)
						{
							cursors.children('.inner-tooltip').stop(true).fadeTo('fast', 0);
						}
						hovering = false;

					});
				}
				cursors.children('.inner-tooltip').hide();
			}

			// Function to normalize value according to min/max and step
			function normalizeValue(value, def)
			{
				// Format
				if (typeof value !== 'number')
				{
					value = parseFloat(value, 10) || def;
				}

				// Range
				value = Math.max(settings.min, Math.min(settings.max, value));

				// If cursor should stick to rounded values
				if (!dragging || settings.stickToRound)
				{
					value = roundValue(value);
				}

				// If cursor should stick to steps
				if (!dragging || settings.stickToStep)
				{
					value = applyStep(value);
				}

				return value;
			};

			// Apply step interval
			function applyStep(value)
			{
				var i, last = false;

				if (settings.step)
				{
					if (typeof settings.step === 'object')
					{
						for (i = 0; i < settings.step.length; ++i)
						{
							// If lower than next step
							if (value < settings.step[i])
							{
								// If no previous value, use this step
								if (last === false)
								{
									return settings.step[i];
								}
								else
								{
									// Return closest value
									return (value-last < settings.step[i]-value) ? last : settings.step[i];
								}
							}

							// Store for next round
							last = settings.step[i];
						}

						// Higher than all steps, use last one
						return last;
					}
					else
					{
						return Math.round((value-settings.min)/settings.step)*settings.step+settings.min;
					}
				}

				return value;
			}

			// Round value
			function roundValue(value)
			{
				// Round
				if (settings.round === true || settings.round === 0)
				{
					value = Math.round(value);
				}
				else if (settings.round > 0)
				{
					value = Math.round(value*Math.pow(10, settings.round))/Math.pow(10, settings.round);
				}

				return value;
			}

			// Return final value
			function finalizeValue(value)
			{
				// If cursor does not stick to rounded values
				if (dragging && !settings.stickToRound)
				{
					value = roundValue(value);
				}

				// If cursor does not stick to steps
				if (dragging && !settings.stickToStep)
				{
					value = applyStep(value);
				}

				return value;
			}

			// Function to set slider value
			function setValue(val1, val2)
			{
				var empty1 = (val1 === undefined || val1 === null),
					empty2 = (val2 === undefined || val2 === null),
					changed = false,
					temp, change1, change2,
					changed1, changed2,
					focus1, focus2;

				// Normalize values
				val1 = empty1 ? (range ? value[0] : value) : normalizeValue(val1, settings.min);
				if (range)
				{
					val2 = empty2 ? value[1] : normalizeValue(val2, settings.max);

					// If val2 is smaller than val1
					if (val2 < val1)
					{
						// If one value is not set, normalize
						if (empty2)
						{
							val1 = val2;
						}
						else if (empty1)
						{
							val2 = val1;
						}
						// If both are defined, change order
						else
						{
							temp = val2;
							val2 = val1;
							val1 = temp;
						}
					}
				}

				// Update values, inputs and cursors
				if (range)
				{
					// Detect change
					change1 = (value[0] != val1);
					change2 = (value[1] != val2);
					if (change1 || change2 || !init)
					{
						// Store
						value = [val1, val2];

						// Final values
						finalVal1 = (change1 || !init) ? finalizeValue(val1) : finalValue[0];
						finalVal2 = (change2 || !init) ? finalizeValue(val2) : finalValue[1];

						// Detect change
						changed1 = (finalValue[0] != finalVal1);
						changed2 = (finalValue[1] != finalVal2);
						changed = (changed1 || changed2 || !init);
						if (changed)
						{
							finalValue = [finalVal1, finalVal2];
						}

						// Cursors
						updateCursor(cursor1, value[0], finalValue[0]);
						updateCursor(cursor2, value[1], finalValue[1]);

						// Bar
						updateBar(value[0], value[1]);

						// Inputs
						if (inputs)
						{
							// Do the inputs have focus?
							focus1 = inputs[0].is(':focus');
							focus2 = inputs[1].is(':focus');

							// Update value
							if (!focus1)
							{
								inputs[0].val(finalValue[0]);
							}
							if (!focus2)
							{
								inputs[1].val(finalValue[1]);
							}

							// Prevent recursion
							if (init && useEvents)
							{
								useEvents = false;
								if (changed1 && !focus1)
								{
									inputs[0].trigger('change');
								}
								if (changed2 && !focus2)
								{
									inputs[1].trigger('change');
								}
								useEvents = true;
							}
						}

						// Callback
						if (init && settings.onChange)
						{
							settings.onChange.call(target[0], finalValue[0], finalValue[1]);
						}
					}
				}
				else
				{
					// Detect change
					change = (value != val1 || !init);
					if (change)
					{
						// Store
						value = val1;

						// Final values
						finalVal1 = finalizeValue(val1);

						// Detect change
						changed = (finalValue != finalVal1 || !init);
						if (changed)
						{
							finalValue = finalVal1;
						}

						// Cursor
						updateCursor(cursor1, value, finalValue);

						// Bar
						updateBar(barModeMin ? settings.min : value, barModeMin ? value : settings.max);

						// Input
						if (inputs)
						{
							// Does the input has focus?
							if (!inputs.is(':focus'))
							{
								// Update value
								inputs.val(finalVal1);

								// Prevent recursion
								if (init && useEvents)
								{
									useEvents = false;
									inputs.trigger('change');
									useEvents = true;
								}
							}
						}

						// Callback
						if (init && settings.onChange)
						{
							settings.onChange.call(target[0], finalVal1);
						}
					}
				}

				return changed;
			};

			// Watch size for fluid elements
			if (!target[0].style.width || !target[0].style.width.match(/[0-9\.]+(px|em)/i))
			{
				target.sizechange(function()
				{
					// Disable animation
					init = false;

					// Refresh size cache
					size = horizontal ? target.innerWidth() : target.innerHeight();

					// Mode
					if (range)
					{
						// Cursors
						updateCursor(cursor1, value[0], finalValue[0]);
						updateCursor(cursor2, value[1], finalValue[1]);

						// Bar
						updateBar(value[0], value[1]);
					}
					else
					{
						// Cursor
						updateCursor(cursor1, value, finalValue);

						// Bar
						updateBar(barModeMin ? settings.min : value, barModeMin ? value : settings.max);
					}

					// Re-enable animation
					init = true;
				});
			};

			// Tell if the cursor move should be animated
			function isAnimated()
			{
				return (settings.animate && init && (!dragging || (settings.step && settings.stickToStep)));
			};

			// Update one cursor position
			function updateCursor(cursor, value, finalValue)
			{
				var animated = isAnimated(),
					tooltip = cursor.children('.inner-tooltip'),
					animated = isAnimated(),
					tooltipValue,
					prop = {};

				// Move
				cursor.stop(true);
				if (horizontal)
				{
					cursor.stop(true)[animated ? 'animate' : 'css']({
						left: Math.round((value-settings.min)/(settings.max-settings.min)*(size-cursor.outerWidth(true)))+'px'
					}, animated ? settings.animateSpeed : null);
				}
				else
				{
					cursor.stop(true)[animated ? 'animate' : 'css']({
						bottom: Math.round((value-settings.min)/(settings.max-settings.min)*(size-cursor.outerHeight(true)))+'px'
					}, animated ? settings.animateSpeed : null);
				}

				// Display value
				if (typeof settings.tooltipFormat === 'string' && settings.tooltipFormat.length > 0)
				{
					tooltipValue = settings.tooltipFormat.replace('[value]', finalValue);
				}
				else if (typeof settings.tooltipFormat === 'function')
				{
					tooltipValue = settings.tooltipFormat(finalValue);
				}
				else
				{
					tooltipValue = finalValue;
				}

				// Tootip
				if (useTooltips)
				{
					tooltip.children('.tooltip-value').text(tooltipValue);
					if (!tooltip.hasClass('left') && !tooltip.hasClass('right'))
					{
						tooltip.css('margin-left', -Math.round(tooltip.outerWidth()/2)+'px');
					}
				}
				else
				{
					// Basic info
					cursor.prop('title', finalValue);
				}
			};

			// Update bar position
			function updateBar(start, end)
			{
				var animated = isAnimated();

				// Set position
				if (horizontal)
				{
					bar.stop(true)[animated ? 'animate' : 'css']({
						left: Math.round((start-settings.min)/(settings.max-settings.min)*size)+'px',
						right: Math.round((settings.max-end)/(settings.max-settings.min)*size)+'px'
					}, animated ? settings.animateSpeed : null);
				}
				else
				{
					bar.stop(true)[animated ? 'animate' : 'css']({
						bottom: Math.round((start-settings.min)/(settings.max-settings.min)*size)+'px',
						top: Math.round((settings.max-end)/(settings.max-settings.min)*size)+'px'
					}, animated ? settings.animateSpeed : null);
				}
			}

			// First setup
			setValue(null, null);

			// Clickable track
			if (settings.clickableTrack)
			{
				target.click(function(event)
				{
					// Only handle clicks on the track
					if (event.target !== this && event.target !== bar[0])
					{
						return;
					}

					var offset = target.offset(),
						position = horizontal ? event.pageX-offset.left : offset.top+size-event.pageY,
						posValue = settings.min+((position/size)*(settings.max-settings.min)),
						closeToFirst;

					// Mode
					if (range)
					{
						// Find closest cursor
						closeToFirst = (posValue < (value[0]+value[1])/2);
						setValue(closeToFirst ? posValue : null, closeToFirst ? null : posValue);
					}
					else
					{
						setValue(posValue);
					}
				});
			}

			// Cursors handling
			cursors.on('touchstart mousedown', function(event)
			{
				// Get initial values
				var element = $(this).addClass('dragging'),
					touchEvent = (event.type === 'touchstart'),
					offsetHolder = touchEvent ? event.originalEvent.touches[0] : event,
					mouseX = offsetHolder.pageX,
					mouseY = offsetHolder.pageY,
					initialPosition = horizontal ? element.parseCSSValue('left') : element.parseCSSValue('bottom'),
					tooltip = element.children('.inner-tooltip'),
					effectOnTooltip = false,
					ieSelectStart;

				// Stop text selection
				event.preventDefault();
				ieSelectStart = document.onselectstart;
				document.onselectstart = function()
				{
					return false;
				}

				// Start dragging
				dragging = true;

				// Show tooltip for touch devices
				if (useTooltips && settings.tooltipOnHover && Modernizr.touch)
				{
					tooltip.fadeIn();
				}

				// Zoom on tooltip
				if (useTooltips && settings.tooltipBiggerOnDrag && tooltip.hasClass('compact'))
				{
					tooltip.removeClass('compact');
					if (!tooltip.hasClass('left') && !tooltip.hasClass('right'))
					{
						tooltip.css('margin-left', -Math.round(tooltip.outerWidth()/2)+'px');
					}
					effectOnTooltip = true;
				}

				// Watch mouse/finger move
				function watchMouse(event)
				{
					var availableSpace = size-(horizontal ? element.outerWidth(true) : element.outerHeight(true)),
						offsetHolder = touchEvent ? event.originalEvent.touches[0] : event,
						position = Math.max(0, Math.min(availableSpace, initialPosition+(horizontal ? offsetHolder.pageX-mouseX : mouseY-offsetHolder.pageY))),
						value = settings.min+((position/availableSpace)*(settings.max-settings.min));

					// Cursor value
					setValue(
						(!range || element.data('slider-range-index') == 0) ? value : null,
						(range && element.data('slider-range-index') == 1) ? value : null
					);
				};
				doc.on(touchEvent ? 'touchmove' : 'mousemove', watchMouse);

				// Watch for mouseup/touchend
				function endDrag()
				{
					doc.off(touchEvent ? 'touchmove' : 'mousemove', watchMouse);
					doc.off(touchEvent ? 'touchend' : 'mouseup', endDrag);

					// End dragging
					dragging = false;
					element.removeClass('dragging');

					// Finalize position if needed
					if (settings.step && !settings.stickToStep)
					{
						if (range)
						{
							setValue(value[0], value[1]);
						}
						else
						{
							setValue(value);
						}
					}

					// Restore tooltip size
					if (effectOnTooltip)
					{
						tooltip.addClass('compact');
						if (!tooltip.hasClass('left') && !tooltip.hasClass('right'))
						{
							tooltip.css('margin-left', -Math.round(tooltip.outerWidth()/2)+'px');
						}
					}

					// Tooltips hide if out of cursor
					if (useTooltips && settings.tooltipOnHover && !hovering)
					{
						tooltip.fadeOut();
					}

					// Re-enable text selection
					if (ieSelectStart)
					{
						document.onselectstart = ieSelectStart;
					}
				};
				doc.on(touchEvent ? 'touchend' : 'mouseup', endDrag);
			});

			// Set as inited
			init = true;

			// Create interface
			target.data('slider', {

				setValue: setValue

			});
		});

		return this;
	};

	/**
	 * Default slider options
	 */
	$.fn.slider.defaults = {
		/**
		 * Minimum value
		 * @var number
		 */
		min: 0,

		/**
		 * Maximum value
		 * @var number
		 */
		max: 100,

		/**
		 * True to round value, a number to set the float length, or false to not round at all
		 * @var boolean|int
		 */
		round: true,

		/**
		 * Will stick cursor to the closest rounded value when dragging
		 * @var boolean
		 */
		stickToRound: false,

		/**
		 * Size of each interval between min and max, or a list of points to snap the cursor to
		 * @var number|array
		 */
		step: null,

		/**
		 * Will stick cursor to the closest step value when dragging
		 * @var boolean
		 */
		stickToStep: true,

		/**
		 * Start value (ignored in range mode)
		 * @var number
		 */
		value: 50,

		/**
		 * Start values (if set and has 2 elements, enable range)
		 * @var array(number, number)
		 */
		values: null,

		/**
		 * Input or jQuery selector for input in which to retrieve/save the slider value (ignored in range mode)
		 * @var jQuery|string
		 */
		input: null,

		/**
		 * Inputs or jQuery selector for inputs in which to retrieve/save the slider values (if set and has 2 elements, enable range)
		 * @var array(jQuery|string, jQuery|string)|string
		 */
		inputs: null,

		/**
		 * Use true to hide related input(s)
		 * @var boolean
		 */
		hideInput: true,

		/**
		 * Orientation of the slider ('horizontal' or 'vertical')
		 * @var string
		 */
		orientation: 'horizontal',

		/**
		 * Class or list of class for the bar
		 * @var string|array
		 */
		barClasses: null,

		/**
		 * Mode of progress bar (only for single value) : 'min' to range from left to cursor, 'max' to range from right to cursor
		 * @var string
		 */
		barMode: 'min',

		/**
		 * Set to true to show inner marks over the bar
		 * @var boolean
		 */
		innerMarksOverBar: false,

		/**
		 * Enable animated stripes
		 * @var boolean
		 */
		stripes: false,

		/**
		 * True for animated stripes (only on compatible browsers)
		 * @var boolean
		 */
		animatedStripes: true,

		/**
		 * True for dark stripes, false for white stripes
		 * @var boolean
		 */
		darkStripes: true,

		/**
		 * Stripes size: 'big', 'normal' or 'thin'
		 * @var string
		 */
		stripesSize: 'normal',

		/**
		 * Set to true to use knobs as handles
		 * @var boolean
		 */
		knob: false,

		/**
		 * Animate cursors moves
		 * @var boolean
		 */
		animate: true,

		/**
		 * Speed for animations (any jquery valid value)
		 * @var string|int
		 */
		animateSpeed: 'fast',

		/**
		 * Position to show the tooltip with current value, 'auto' for 'top' if the lider is horizontal and 'right' if vertical,
		 * or false to disable. For range sliders, use an array of two values - one for each cursor
		 * @var string|array|boolean
		 */
		tooltip: 'auto',

		/**
		 * Format of tooltip text: null for bare value, pattern string (with [value] as a placeholder)
		 * or a function(value)
		 * @var string|function
		 */
		tooltipFormat: null,

		/**
		 * Always show tooltip or show only on hover
		 * @var boolean
		 */
		tooltipOnHover: true,

		/**
		 * Make tooltip bigger on drag (only if it has class 'compact')
		 * @var boolean
		 */
		tooltipBiggerOnDrag: true,

		/**
		 * Class or list of class for the tooltip
		 * @var string|array
		 */
		tooltipClass: ['compact', 'black-gradient', 'glossy'],

		/**
		 * Enable quick value selection by clicking on the background
		 * @var boolean
		 */
		clickableTrack: true,

		/**
		 * Callback when the slider value is changed, takes one param for value, two for ranges
		 * @var function
		 */
		onChange: null
	};

	/**
	 * Set slider current value(s)
	 * @param number val1 the value for single value sliders, or the first handle value for ranges
	 * @param number val2 the value for the second handle (only for ranges)
	 */
	$.fn.setSliderValue = function(val1, val2)
	{
		return this.each(function()
		{
			var target = $(this),
				data = target.data('slider');

			// If valid
			if (data)
			{
				data.setValue(val1, val2);
			}
		});
	};

	/**
	 * Create a progress bar in the target element
	 * Options may be set using the inline html5 data-progress-options attribute:
	 * <div data-progress-options="{'max':200}"></div>
	 * @param int val the progress value (can be ommitted and defined in options or available as text inside the target)
	 * @param object options
	 */
	$.fn.progress = function(val, options)
	{
		// Arguments
		if (typeof val === 'object' && val !== null)
		{
			options = val;
			val = null;
		}

		this.each(function()
		{
				// Target
			var target = $(this),
				value = val,

				// Progress size
				size,

				// Inline options
				userOptions = $.extend({}, options, target.data('progress-options')),

				// Final settings
				settings = $.extend({}, $.fn.progress.defaults, userOptions),

				// Is the progress horizontal ?
				horizontal = (settings.orientation.toLowerCase() !== 'vertical'),

				// List of classes
				barClasses = ['progress-bar'],

				// Bar mode
				barModeMin = (settings.barMode.toLowerCase() !== 'max'),

				// Bar object
				bar,

				// Value text wrappers
				texts = $(),

				// Stripes
				stripes = false,

				// Work vars
				showText, init = false,

				// Function to normalize value according to min/max and step
				normalizeValue = function(value)
				{
					// Format
					if (typeof value !== 'number')
					{
						value = parseFloat(value, 10) || settings.min;
					}

					// Range
					value = Math.max(settings.min, Math.min(settings.max, value));

					// Round
					value = roundValue(value);

					// Steps
					value = applyStep(value);

					return value;
				},

				// Apply step interval
				applyStep = function(value)
				{
					var i, last = false;

					if (settings.step)
					{
						if (typeof settings.step === 'object')
						{
							for (i = 0; i < settings.step.length; ++i)
							{
								// If lower than next step
								if (value < settings.step[i])
								{
									// If no previous value, use this step
									if (last === false)
									{
										return settings.step[i];
									}
									else
									{
										// Return closest value
										return (value-last < settings.step[i]-value) ? last : settings.step[i];
									}
								}

								// Store for next round
								last = settings.step[i];
							}

							// Higher than all steps, use last one
							return last;
						}
						else
						{
							return Math.round((value-settings.min)/settings.step)*settings.step+settings.min;
						}
					}

					return value;
				},

				// Round value
				roundValue = function(value)
				{
					// Round
					if (settings.round === true || settings.round === 0)
					{
						value = Math.round(value);
					}
					else if (settings.round > 0)
					{
						value = Math.round(value*Math.pow(10, settings.round))/Math.pow(10, settings.round);
					}

					return value;
				},

				// Function to set progress value
				setValue = function(val, showValue)
				{
					var rawValue = val,
						changed = false,
						temp, change;

					// Parse
					if (typeof val !== 'number')
					{
						if (val && val.length > 0)
						{
							val = parseFloat(val);
							if (isNaN(val))
							{
								val = settings.value;
							}
						}
						else
						{
							val = settings.value;
						}
					}

					// Normalize value
					val = normalizeValue(val);

					// Detect change
					change = (value != val || !init);
					if (change)
					{
						// Store
						value = val;

						// Values
						if (showValue === true || ((showValue === null || showValue == undefined) && settings.showValue))
						{
							texts.text(rawValue);
						}
						else
						{
							texts.empty();
						}

						// Update bar
						updateBar(barModeMin ? settings.min : value, barModeMin ? value : settings.max);
					}

					return change;
				},

				// Tell if the cursor move should be animated
				isAnimated = function()
				{
					return (settings.animate && init);
				},

				// Update bar
				updateBar = function(start, end)
				{
					var animated = isAnimated();

					// Set position
					if (horizontal)
					{
						bar.stop(true)[animated ? 'animate' : 'css']({
							left: Math.round((start-settings.min)/(settings.max-settings.min)*size)+'px',
							right: Math.round((settings.max-end)/(settings.max-settings.min)*size)+'px'
						}, animated ? settings.animateSpeed : null);
					}
					else
					{
						bar.stop(true)[animated ? 'animate' : 'css']({
							bottom: Math.round((start-settings.min)/(settings.max-settings.min)*size)+'px',
							top: Math.round((settings.max-end)/(settings.max-settings.min)*size)+'px'
						}, animated ? settings.animateSpeed : null);
					}
				},

				// Show stripes
				showStripes = function(extraOptions)
				{
					var stripesSettings = extraOptions ? settings : $.extend({}, settings, extraOptions),
						stripesSize, animatedStripes, darkStripes;

					// If not already set
					if (!stripes)
					{
						// Dark or not
						darkStripes = stripesSettings.darkStripes ? 'dark-' : '';

						// Size
						stripesSize = (stripesSettings.stripesSize === 'big' || stripesSettings.stripesSize === 'thin') ? stripesSettings.stripesSize+'-' : '';

						// Animated
						animatedStripes = stripesSettings.animatedStripes ? ' animated' : '';

						// Final
						stripes = $('<span class="'+darkStripes+stripesSize+'stripes'+animatedStripes+'"></span>').appendTo(bar);

						// Animation
						if (init)
						{
							stripes.hide().fadeIn();
						}
					}
				},

				// Hide stripes
				hideStripes = function()
				{
					// If set
					if (stripes)
					{
						// Animation
						stripes.fadeOut(function()
						{
							$(this).remove();
						});
						stripes = false;
					}
				},

				// Change bar classes
				changeBarColor = function(color, glossy)
				{
					// Remove previous colors
					bar.removeClass('silver-gradient black-gradient anthracite-gradient grey-gradient white-gradient red-gradient orange-gradient green-gradient blue-gradient');

					// Set new one
					bar.addClass(color);

					// Glossy
					if (glossy === true || glossy === false)
					{
						bar[glossy ? 'addClass' : 'removeClass']('glossy');
					}
				};

			// Value not given
			if (value === null || value == undefined)
			{
				// Check if set as inner text
				value = $.trim(target.text());
			}
			target.empty();

			// Track size
			if (settings.style && (settings.style === 'thin' || settings.style === 'large'))
			{
				target.addClass(settings.style);
			}

			// Should we display the value as text?
			showText = (horizontal && !target.hasClass('thin')) || (!horizontal && target.hasClass('large'));

			// Background text
			if (showText)
			{
				texts = texts.add($('<span class="progress-text"></span>').appendTo(target));
			}

			// Progress base
			target.addClass('progress').buildTrack(settings);
			size = horizontal ? target.innerWidth() : target.innerHeight();

			// Bar
			if (typeof settings.barClasses === 'string')
			{
				barClasses.push(settings.barClasses);
			}
			else if (typeof settings.barClasses === 'object')
			{
				barClasses = barClasses.concat(settings.barClasses);
			}
			bar = $('<span class="'+barClasses.join(' ')+'"></span>');
			bar[settings.innerMarksOverBar ? 'prependTo' : 'appendTo'](target);

			// Main text
			if (showText)
			{
				texts = texts.add($('<span class="progress-text"></span>').appendTo(bar));
			}

			// Stripes
			if (settings.stripes)
			{
				showStripes();
			}

			// Watch size for fluid elements
			if (!target[0].style.width || !target[0].style.width.match(/[0-9\.]+(px|em)/i))
			{
				target.on(horizontal ? 'widthchange' : 'heightchange', function()
				{
					// Disable animation
					init = false;

					// Refresh size cache
					size = horizontal ? target.innerWidth() : target.innerHeight();

					// Bar
					updateBar(barModeMin ? settings.min : value, barModeMin ? value : settings.max);

					// Re-enable animation
					init = true;
				});
			};

			// First setup
			setValue(value);

			// Set as inited
			init = true;

			// Create interface
			target.data('progress', {

				setValue: setValue,
				showStripes: showStripes,
				hideStripes: hideStripes,
				changeBarColor: changeBarColor

			});
		});

		return this;
	};

	/**
	 * Default progress options
	 */
	$.fn.progress.defaults = {

		/**
		 * Minimum value
		 * @var number
		 */
		min: 0,

		/**
		 * Maximum value
		 * @var number
		 */
		max: 100,

		/**
		 * True to round value, a number to set the float length, or false to not round at all
		 * @var boolean|int
		 */
		round: true,

		/**
		 * Size of each interval between min and max, or a list of points to snap the progress bar to
		 * @var number|array
		 */
		step: null,

		/**
		 * Progress value (only used if not passed as parameter or given as text in the target element)
		 * @var number
		 */
		value: 0,

		/**
		 * Orientation of the progress ('horizontal' or 'vertical')
		 * @var string
		 */
		orientation: 'horizontal',

		/**
		 * Track size ('thin', 'large' or empty for normal)
		 * @var string
		 */
		style: null,

		/**
		 * True to show value, false to hide
		 * @var boolean
		 */
		showValue: true,

		/**
		 * Class or list of class for the bar
		 * @var string|array
		 */
		barClasses: null,

		/**
		 * Mode of progress bar : 'min' to range from left to value, 'max' to range from value to right
		 * @var string
		 */
		barMode: 'min',

		/**
		 * Set to true to show inner marks over the bar
		 * @var boolean
		 */
		innerMarksOverBar: false,

		/**
		 * Enable animated stripes
		 * @var boolean
		 */
		stripes: false,

		/**
		 * True for animated stripes (only on compatible browsers)
		 * @var boolean
		 */
		animatedStripes: true,

		/**
		 * True for dark stripes, false for white stripes
		 * @var boolean
		 */
		darkStripes: true,

		/**
		 * Stripes size: 'big', 'normal' or 'thin'
		 * @var string
		 */
		stripesSize: 'normal',

		/**
		 * Animate bar moves
		 * @var boolean
		 */
		animate: true,

		/**
		 * Speed for animations (any jquery valid value)
		 * @var string|int
		 */
		animateSpeed: 'fast'
	};

	/**
	 * Set progress current value
	 * @param number value the value, may contain an unit
	 * @param boolean|null showValue true to show text, false to hide, or leave empty to use original settings value (optional)
	 */
	$.fn.setProgressValue = function(value, showValue)
	{
		return this.each(function()
		{
			var target = $(this),
				data = target.data('progress');

			// If valid
			if (data)
			{
				data.setValue(value, showValue);
			}
		});
	};

	/**
	 * Show stripes on progress bar
	 * @param object options any option to override the inital settings (see progress() defaults for more details) (optional)
	 */
	$.fn.showProgressStripes = function(options)
	{
		return this.each(function()
		{
			var target = $(this),
				data = target.data('progress');

			// If valid
			if (data)
			{
				data.showStripes(options);
			}
		});
	};

	/**
	 * Hide stripes on progress bar
	 */
	$.fn.hideProgressStripes = function()
	{
		return this.each(function()
		{
			var target = $(this),
				data = target.data('progress');

			// If valid
			if (data)
			{
				data.hideStripes();
			}
		});
	};

	/**
	 * Change progress bar color (only works with gradients)
	 * @param string color the new gradient color class
	 * @param boolean glossy true of false, or leave empty to keep current style (optional)
	 */
	$.fn.changeProgressBarColor = function(color, glossy)
	{
		return this.each(function()
		{
			var target = $(this),
				data = target.data('progress');

			// If valid
			if (data)
			{
				data.changeBarColor(color, glossy);
			}
		});
	};

})(jQuery, document);
/**
 *
 * '||''|.                            '||
 *  ||   ||    ....  .... ...   ....   ||    ...   ... ...  ... ..
 *  ||    || .|...||  '|.  |  .|...||  ||  .|  '|.  ||'  ||  ||' ''
 *  ||    || ||        '|.|   ||       ||  ||   ||  ||    |  ||
 * .||...|'   '|...'    '|     '|...' .||.  '|..|'  ||...'  .||.
 *                                                  ||
 * --------------- By Display:inline ------------- '''' -----------
 *
 * Tooltip plugin
 *
 * Structural good practices from the article from Addy Osmani 'Essential jQuery plugin patterns'
 * @url http://coding.smashingmagazine.com/2011/10/11/essential-jquery-plugin-patterns/
 */

/*
 * The semi-colon before the function invocation is a safety
 * net against concatenated scripts and/or other plugins
 * that are not closed properly.
 */
;(function($, window, document)
{
	/*
	 * document is passed through as local variable rather than as global, because this (slightly)
	 * quickens the resolution process and can be more efficiently minified.
	 */

		// Objects cache
	var win = $(window),
		doc = $(document),

		// Current exclusive tooltip
		exclusive = false;

	/**
	 * Internal reference: the div holding standard tooltips
	 * @var jQuery
	 */
	var _standardTooltipsDiv = false;

	/**
	 * Internal function: retrieve the div holding standard tooltips
	 *
	 * @return jQuery the div selection
	 */
	function _getStandardTooltipsDiv()
	{
		if (!_standardTooltipsDiv)
		{
			_standardTooltipsDiv = $('<div id="tooltips"></div>').appendTo(document.body);
		}

		return _standardTooltipsDiv;
	}

	/**
	 * Internal reference: the div holding tooltips over modals and notifications
	 * @var jQuery
	 */
	var _overTooltipsDiv = false;

	/**
	 * Internal function: retrieve the div holding tooltips over modals and notifications
	 *
	 * @return jQuery the div selection
	 */
	function _getOverTooltipsDiv()
	{
		if (!_overTooltipsDiv)
		{
			_overTooltipsDiv = $('<div id="tooltips-over"></div>').appendTo(document.body);
		}

		return _overTooltipsDiv;
	}

	/**
	 * Check if a content is valid
	 * @param mixed content the value to check
	 * @return boolean true if valid, else false
	 */
	function _isValidContent(content)
	{
		return ((content instanceof jQuery) || typeof content === 'function' || (typeof content === 'string' && $.trim(content).length > 0));
	}

	/**
	 * Parse the content or try to extract it from the element
	 * @param mixed content (see tooltip() for details)
	 * @param jQuery target the target element
	 * @return string|jQuery|boolean the content, or false if none
	 */
	function _parseContent(content, target)
	{
		var title, children;

		// If valid
		if (_isValidContent(content))
		{
			return content;
		}

		// Test if content set as data-tooltip-content
		content = target.data('tooltip-content');
		if (_isValidContent(content))
		{
			// Clear
			if (target[0].title && target[0].title.length)
			{
				target[0].title = '';
				target.data('tooltip-title', {
					value:		content,
					element:	target[0]
				});
			}

			return content;
		}

		// Test if there is a stored title
		title = target.data('tooltip-title');
		if (title)
		{
			return title.value;
		}

		// Check title attribute
		if (target[0].title && $.trim(target[0].title).length > 0)
		{
			content = target[0].title;
			target[0].title = '';
			target.data('tooltip-title', {
				value:		content,
				element:	target[0]
			});

			return content;
		}

		// For elements with an unique child, use the child title
		children = target.children();
		if (children.length === 1 && children[0].title && $.trim(children[0].title).length > 0)
		{
			content = children[0].title;
			children[0].title = '';
			target.data('tooltip-title', {
				value:		content,
				element:	children[0]
			});

			return content;
		}

		// No content
		return false;
	}

	/**
	 * Restore element's title if needed
	 * @param jQuery target the target element
	 * @return void
	 */
	function _restoreTitle(target)
	{
		// Test if there is a stored title
		var title = target.data('tooltip-title');
		if (title)
		{
			title.element.title = title.value;
			target.removeData('tooltip-title');
		}
	}

	/**
	 * Display a tooltip over an element. If the page is not yet ready, delay the tooltip until it is ready.
	 *
	 * @var string|function|jQuery content a text or html content to display, or a function to run on the element to get the content
	 * (can be omitted, auto-detect if not defined or empty)
	 * @var object options an object with any options for the tooltip - optional (see defaults for more details). If not set, the function
	 * will try to retrieve any option of an existing or delayed tooltip on the same element, so when changing the content of a tooltip
	 * just call the function without options
	 */
	$.fn.tooltip = function(content, options)
	{
		// Settings
		var globalSettings = $.extend({}, $.fn.tooltip.defaults, options),

			// If no options were given
			noOptions = false;

		// Options format
		if (typeof content === 'object' && !(content instanceof jQuery))
		{
			options = content;
			content = '';
		}
		if (!options || typeof options !== 'object')
		{
			noOptions = true;
			options = {};
		}

		// Initial setup
		this.each(function(i)
		{
				// Tooltip target
			var target = $(this),

				// Is the target a replacement element?
				replacement = target.data('replaced'),

				// Inline settings
				inlineOptions = target.data('tooltip-options') || (replacement ? (replacement.data('tooltip-options') || {}) : {}),

				// Check if a tooltip is delayed for creation
				awaiting = target.data('tooltip-awaiting'),

				// Ajax promise (if any) and loaded data
				promise = false,

				// Functions
				onMouseleave, onBlur, onClick;

			/*
			 * If the document is not ready or we want some delay
			 */
			if (!$.isReady || (!options.ignoreDelay && (options.delay > 0 || inlineOptions.delay > 0)))
			{
				var delay = inlineOptions.delay || options.delay || 40,

					// Options
					thisOptions = options,

					// Timeout ID
					timeout,

					// Functions
					abort;

				// Parse content
				content = _parseContent(content, target);

				// If there is already a delayed tooltip
				if (awaiting)
				{
					// Stop timeout
					if (awaiting.abort() === false)
					{
						return;
					}

					// Merge options
					if (noOptions)
					{
						thisOptions = $.extend({}, awaiting.options);
					}
				}

				// Close on mouseleave
				if (thisOptions.removeOnMouseleave)
				{
					// Callback function
					onMouseleave = function(event)
					{
						// Abort tooltip
						abort();
					};

					// Bind
					target.on('mouseleave', onMouseleave);
				}

				// Close on click anywhere else
				if (thisOptions.removeOnBlur)
				{
					// Callback function
					onBlur = function(event)
					{
						// Abort tooltip
						abort();
					};

					// Bind
					doc.on('click touchend', onBlur);
				}

				// Function to abort tooltip
				abort = function(force, doNotRestore)
				{
					// Callback
					if (thisOptions.onAbort)
					{
						if (settings.onAbort.call(tooltip[0], target) === false && !force)
						{
							return false;
						}
					}

					// Stop timeout
					clearTimeout(timeout);

					// Clear data
					target.removeData('tooltip-awaiting');

					// Listeners
					if (onMouseleave)
					{
						target.off('mouseleave', onMouseleave);
					}
					if (onBlur)
					{
						doc.off('click touchend', onBlur);
					}

					// Stored title
					if (!doNotRestore)
					{
						_restoreTitle(target);
					}
				};

				// Store
				target.data('tooltip-awaiting', {
					options: thisOptions,
					abort: abort
				});

				// Delay
				timeout = setTimeout(function()
				{
					abort(false, true);
					target.tooltip(content, $.extend(thisOptions, { ignoreDelay: true }));

				}, delay);
			}
			/*
			 * Show tooltip
			 */
			else
			{
					// Check if a tooltip already exists
				var previous = target.data('tooltip'),

					// Previous tooltip settings
					previousSettings = {},

					// If there is a previous tooltip, do not animate
					skipAnimation = false,

					// Options from the delayed tooltip
					awaitingOptions = {},

					// Options
					settings,

					// Objects
					parent, tooltip, arrow, optionHolder,

					// Dom working
					dom, domHidden = false, placeholder,

					// Work vars
					noPointerEvents, arrowOffset, animValues, removeAnimValues,

					// Functions
					updatePosition, removeTooltip, endRemove;

				// If a tooltip already exists
				if (previous)
				{
					// If blocking, exit
					if (previous.settings.lock && (noOptions || !options.fromAjax))
					{
						return;
					}

					// Remove
					if (previous.removeTooltip(false, true) === false)
					{
						return;
					}

					// Retrieve previous settings
					if (noOptions)
					{
						previousSettings = previous.settings;
					}

					// Skip animation
					skipAnimation = true;
				}

				// If another tooltip is awaiting
				if (awaiting)
				{
					// If blocking, exit
					if (awaiting.options.lock)
					{
						return;
					}

					// Abort
					if (awaiting.abort() === false)
					{
						return;
					}

					// Retrieve options
					if (noOptions)
					{
						awaitingOptions = awaiting.options;
					}
				}

				// Check for tooltip alignement classes
				if (!options.position && !previousSettings.position && !awaitingOptions.position)
				{
					optionHolder = target.closest('.tooltip-top, .tooltip-right, .tooltip-bottom, .tooltip-left');
					if (optionHolder.length > 0)
					{
						awaitingOptions.position = /tooltip-(top|right|bottom|left)/.exec(optionHolder[0].className)[1];
					}
				}

				// Final settings
				settings = $.extend({}, globalSettings, inlineOptions, previousSettings, awaitingOptions);

				// Ajax loading
				if (settings.ajax && !settings.fromAjax)
				{
					// Mode
					if (typeof settings.ajax === 'object')
					{
						promise = settings.ajax;
					}
					else
					{
						promise = $.ajax(settings.ajax, settings.ajaxOptions);
					}

					// Prevent loading again by next tooltip
					settings.fromAjax = true;

					// On success
					promise.done(function(data)
					{
						// Check if tooltip is still visible
						var current = target.data('tooltip');
						if (current)
						{
							target.tooltip(data, settings);
						}
					});

					// On error
					promise.fail(function()
					{
						// Check if tooltip is still visible
						var current = target.data('tooltip');
						if (current)
						{
							target.tooltip(settings.ajaxErrorMessage, settings);
						}
					});
				}

				// If content is a function
				if (typeof content === 'function')
				{
					content = content.apply(this);
				}

				// Parse content
				content = _parseContent(content, target);
				if (content === false)
				{
					// No content, abort
					return;
				}
				if (content instanceof jQuery)
				{
					// Use dom element
					dom = content;
					content = '';
				}

				// Init
				if ( settings.local )
				{
					// Search closest block parent
					parent = target.parent();
					while ( !parent.is( 'body' ) && parent.css( 'display' ) !== 'block' )
					{
						parent = parent.parent();
					}

					// Make it positionned
					if ( parent.css( 'position' ) !== 'absolute' && parent.css( 'position' ) !== 'fixed' )
					{
						parent.addClass( 'relative' );
					}
				}
				else
				{
					parent = (target.closest('.notification, .modal').length > 0) ? _getOverTooltipsDiv() : _getStandardTooltipsDiv();
				}
				animateDistance = (settings.animate && !skipAnimation) ? settings.animateMove : 0;

				// If exclusive, remove existing one
				if (settings.exclusive && exclusive)
				{
					// The remove animation is skipped to prevent callbacks to fire in the wrong order
					if (exclusive.removeTooltip(false, true) === false)
					{
						return;
					}
				}

				// Create element
				noPointerEvents = settings.noPointerEvents ? ' no-pointer-events' : '';
				tooltip = $('<div class="message tooltip '+settings.classes.join(' ')+noPointerEvents+'">'+content+'</div>')
							.appendTo(parent)
							.data('tooltip-target', target);

				// Dom content
				if (dom)
				{
					// If hidden
					if (!dom.is(':visible'))
					{
						domHidden = true;
						dom.show();
					}

					// Check if already in the document
					if (dom.parent().length > 0)
					{
						placeholder = $('<span style="display:none"></span>').insertBefore(dom);
						dom.detach();
					}

					// Insert
					tooltip.append(dom);
				}

				// Arrow
				switch (settings.position.toLowerCase())
				{
					case 'right':
						arrow = $('<span class="block-arrow left"><span></span></span>').appendTo(tooltip);
						arrowOffset = arrow.parseCSSValue('margin-top');
						break;

					case 'bottom':
						arrow = $('<span class="block-arrow top"><span></span></span>').appendTo(tooltip);
						arrowOffset = arrow.parseCSSValue('margin-left');
						break;

					case 'left':
						arrow = $('<span class="block-arrow right"><span></span></span>').appendTo(tooltip);
						arrowOffset = arrow.parseCSSValue('margin-top');
						break;

					default:
						arrow = $('<span class="block-arrow"><span></span></span>').appendTo(tooltip);
						arrowOffset = arrow.parseCSSValue('margin-left');
						break;
				}

				// Function to update position
				updatePosition = function()
				{
					var targetpos = target.offset(),
						targetWidth = target.outerWidth(),
						targetHeight = target.outerHeight(),
						tooltipWidth = tooltip.outerWidth(),
						tooltipHeight = tooltip.outerHeight(),
						docWidth = $.template.viewportWidth,
						docHeight = $.template.viewportHeight,
						top, left, offset, position,
						arrowExtraOffset = 0;

					switch (settings.position)
					{
						case 'right':
							// Default position
							top = targetpos.top+Math.round(targetHeight/2)-Math.round(tooltipHeight/2);
							left = targetpos.left+targetWidth+settings.spacing;

							// Bounds check - horizontal
							if (left+tooltipWidth > docWidth-settings.screenPadding)
							{
								// Revert
								left = targetpos.left-tooltipWidth-settings.spacing;
								animateDistance *= -1;
								arrow.removeClass('left').addClass('right');
							}
							else
							{
								arrow.removeClass('right').addClass('left');
							}

							// Bounds check - vertical
							if (top < settings.screenPadding+doc.scrollLeft())
							{
								offset = settings.screenPadding-top;
								arrowExtraOffset = -Math.min(offset, Math.round(tooltipHeight/2)-settings.arrowMargin);
								top += offset;
							}
							else if (top+tooltipHeight > docHeight-settings.screenPadding)
							{
								offset = docHeight-settings.screenPadding-tooltipHeight-top;
								arrowExtraOffset = Math.min(-offset, Math.round(tooltipHeight/2)-settings.arrowMargin);
								left += offset;
							}

							// Animation init
							left -= animateDistance;
							break;

						case 'bottom':
							// Default position
							top = targetpos.top+targetHeight+settings.spacing;
							left = targetpos.left+Math.round(targetWidth/2)-Math.round(tooltipWidth/2);

							// Bounds check - horizontal
							if (left < settings.screenPadding)
							{
								offset = settings.screenPadding-left;
								arrowExtraOffset = -Math.min(offset, Math.round(tooltipWidth/2)-settings.arrowMargin);
								left += offset;
							}
							else if (left+tooltipWidth > docWidth-settings.screenPadding)
							{
								offset = docWidth-settings.screenPadding-tooltipWidth-left;
								arrowExtraOffset = Math.min(-offset, Math.round(tooltipWidth/2)-settings.arrowMargin);
								left += offset;
							}

							// Bounds check - vertical
							if (top+tooltipHeight > docHeight-settings.screenPadding+doc.scrollTop())
							{
								// Revert
								top = targetpos.top-tooltipHeight-settings.spacing;
								animateDistance *= -1;
								arrow.removeClass('top').addClass('bottom');
							}
							else
							{
								arrow.removeClass('bottom').addClass('top');
							}

							// Animation init
							top -= animateDistance;
							break;

						case 'left':
							// Default position
							top = targetpos.top+Math.round(targetHeight/2)-Math.round(tooltipHeight/2);
							left = targetpos.left-tooltipWidth-settings.spacing;

							// Bounds check - horizontal
							if (left < settings.screenPadding+doc.scrollLeft())
							{
								// Revert
								left = targetpos.left+targetWidth+settings.spacing;
								animateDistance *= -1;
								arrow.removeClass('right').addClass('left');
							}
							else
							{
								arrow.removeClass('left').addClass('right');
							}

							// Bounds check - vertical
							if (top < settings.screenPadding)
							{
								offset = settings.screenPadding-top;
								arrowExtraOffset = -Math.min(offset, Math.round(tooltipHeight/2)-settings.arrowMargin);
								top += offset;
							}
							else if (top+tooltipHeight > docHeight-settings.screenPadding)
							{
								offset = docHeight-settings.screenPadding-tooltipHeight-top;
								arrowExtraOffset = Math.min(-offset, Math.round(tooltipHeight/2)-settings.arrowMargin);
								left += offset;
							}

							// Animation init
							left += animateDistance;
							break;

						default:
							// Default position
							top = targetpos.top-tooltipHeight-settings.spacing;
							left = targetpos.left+Math.round(targetWidth/2)-Math.round(tooltipWidth/2);

							// Bounds check - horizontal
							if (left < settings.screenPadding)
							{
								offset = settings.screenPadding-left;
								arrowExtraOffset = -Math.min(offset, Math.round(tooltipWidth/2)-settings.arrowMargin);
								left += offset;
							}
							else if (left+tooltipWidth > docWidth-settings.screenPadding)
							{
								offset = docWidth-settings.screenPadding-tooltipWidth-left;
								arrowExtraOffset = Math.min(-offset, Math.round(tooltipWidth/2)-settings.arrowMargin);
								left += offset;
							}

							// Bounds check - vertical
							if (top < settings.screenPadding+doc.scrollTop())
							{
								// Revert
								top = targetpos.top+targetHeight+settings.spacing;
								animateDistance *= -1;
								arrow.removeClass('bottom').addClass('top');
							}
							else
							{
								arrow.removeClass('top').addClass('bottom');
							}

							// Animation init
							top += animateDistance;
							break;
					}

					// If local
					if ( settings.local )
					{
						// Local coordinates
						position = parent.offset();
						top -= position.top;
						left -= position.left;

						// Set position
						tooltip.css({
							top: top + 'px',
							left: left + 'px'
						});
					}
					else
					{
						// Set position
						tooltip.offset({
							top: top,
							left: left
						});
					}
					if (settings.position === 'left' || settings.position === 'right')
					{
						arrow.css('margin-top', (arrowExtraOffset === 0) ? '' : (arrowOffset+arrowExtraOffset)+'px');
					}
					else
					{
						arrow.css('margin-left', (arrowExtraOffset === 0) ? '' : (arrowOffset+arrowExtraOffset)+'px');
					}
				};

				// Watch movement (will set position)
				tooltip.trackElement(target, updatePosition);
				
				// Added for PRISM
				tooltip.bind("mouseleave",function(){
					// Abort tooltip
					removeTooltip();
				});
				
				// Show animation
				if (settings.animate)
				{
					// Prepare
					animValues = {
						opacity: 1
					};
					removeAnimValues = {
						opacity: 0
					};

					// Move
					if (animateDistance !== 0)
					{
						switch (settings.position)
						{
							case 'right':
								animValues.left = '+='+animateDistance+'px';
								removeAnimValues.left = '-='+animateDistance+'px';
								break;

							case 'bottom':
								animValues.top = '+='+animateDistance+'px';
								removeAnimValues.top = '-='+animateDistance+'px';
								break;

							case 'left':
								animValues.left = '-='+animateDistance+'px';
								removeAnimValues.left = '+='+animateDistance+'px';
								break;

							default:
								animValues.top = '-='+animateDistance+'px';
								removeAnimValues.top = '+='+animateDistance+'px';
								break;
						}

						// Reset initial animation distance for further positioning
						animateDistance = 0;
					}

					// If no previous tip was replaced
					if (!skipAnimation)
					{
						// Here we go!
						tooltip.css({ opacity: 0 }).animate(animValues, settings.animateSpeed);
					}
				}

				// Remove
				removeTooltip = function(force, skipAnimation)
				{
					// Callback
					if (settings.onRemove)
					{
						if (settings.onRemove.call(tooltip[0], target) === false && !force)
						{
							return false;
						}
					}

					// Listeners
					if (onMouseleave)
					{
						target.off('mouseleave', onMouseleave);
					}
					if (onBlur)
					{
						doc.off('click touchend', onBlur);
					}
					if (onClick)
					{
						tooltip.off('click touchend', onClick);
					}

					// Clear data
					target.removeData('tooltip');

					// If exclusive, clear data
					if (settings.exclusive)
					{
						exclusive = false;
					}

					// Animation
					if (settings.animate && !skipAnimation)
					{
						// Remove
						tooltip.addClass('tooltip-removed').animate(removeAnimValues, settings.animateSpeed, endRemove);
					}
					else
					{
						// Finalize
						endRemove();
					}

					return true;
				};

				// Finalize remove
				endRemove = function()
				{
					// Stored title
					_restoreTitle(target);

					// If pulled from the dom
					if (placeholder)
					{
						dom.detach().insertAfter(placeholder);
						placeholder.remove();
					}

					// If hidden
					if (domHidden)
					{
						dom.hide();
					}

					// Remove
					tooltip.remove();
				};

				// Store
				target.data('tooltip', {
					element: tooltip,
					settings: settings,
					updatePosition: updatePosition,
					removeTooltip: removeTooltip
				});

				// If exclusive, store
				if (settings.exclusive)
				{
					exclusive = {
						removeTooltip: removeTooltip,
						dom: dom
					};
				}

				// Close on mouseleave
				if (settings.removeOnMouseleave)
				{
					// Callback function
					onMouseleave = function(event)
					{
						// Remove tooltip
						removeTooltip();
					};

					// Bind
					target.on('mouseleave', onMouseleave);
				}

				// Close on click anywhere else
				if (settings.removeOnBlur)
				{
					// Prevent inner click propagation
					tooltip.on('click touchend', function(event)
					{
						event.stopPropagation();
					});

					// Callback function
					onBlur = function(event)
					{
						// Do not process if default is prevented (most probably trigerred from inside the tooltip)
						if (event.isDefaultPrevented())
						{
							return;
						}

						// Remove tooltip
						removeTooltip();
					};

					// Bind
					doc.on('click touchend', onBlur);
				}

				// Close on click on tooltip
				if (settings.removeOnClick && !settings.noPointerEvents)
				{
					// Callback function
					onClick = function(event)
					{
						// Remove tooltip
						removeTooltip();
					};

					// Bind
					tooltip.on('click touchend', onClick);
				}

				// Callback
				if (settings.onShow)
				{
					settings.onShow.call(tooltip[0], target);
				}
			}
		});

		return this;
	};

	/**
	 * Remove tooltip
	 * @param boolean force use true to close tooltips even when the onClose/onAbort callback functions return false (optional, default: false)
	 * @param boolean skipAnimation use true to disable the close animation (optional, default: false)
	 */
	$.fn.removeTooltip = function(force, skipAnimation)
	{
		this.each(function(i)
		{
			var target = $(this),
				tooltip = target.data('tooltip'),
				awaiting = target.data('tooltip-awaiting'),
				title;

			// If found
			if (tooltip)
			{
				// Remove
				if (tooltip.removeTooltip(force, skipAnimation) === false)
				{
					return;
				}
			}

			// If there is a delayed tooltip
			if (awaiting)
			{
				// Abort
				if (awaiting.abort(force) === false)
				{
					return;
				}
			}
		});

		return this;
	};

	/**
	 * Open a tooltip menu on click on any element
	 * @var string|function|jQuery content a text or html content to display, or a function to run on the element to get the content
	 * @var object options an object with any options for the tooltip - optional (see defaults for more details)
	 * @var string eventName the event on which to open the menu - optional (default: 'click')
	 */
	$.fn.menuTooltip = function(content, options, eventName)
	{
		// Parameters
		eventName = eventName || 'click';

		// Bind event
		this.on(eventName, function(event)
		{
			event.preventDefault();
			event.stopPropagation();

			// Open menu
			$(this).tooltip(content, $.extend({

				lock:				true,
				exclusive:			true,
				removeOnBlur:		true,
				noPointerEvents:	false

			}, options));
		});

		return this;
	};

	/**
	 * Tooltip function defaults
	 * @var object
	 */
	$.fn.tooltip.defaults = {
		/**
		 * Position: 'top', 'right', 'bottom' or 'left'
		 * @var string
		 */
		position: 'top',

		/**
		 * Should the tooltip be inserted locally (in the element's parent) or globally
		 * @var boolean
		 */
		local: false,

		/**
		 * Space between tooltip and the target element
		 * @var int
		 */
		spacing: 10,

		/**
		 * Extra classes (colors...)
		 * @var array
		 */
		classes: [],

		/**
		 * Prevent the tooltip from interacting with mouse
		 * @var boolean
		 */
		noPointerEvents: false,

		/**
		 * When true, prevent any other tooltip to show on the same target
		 * @var boolean
		 */
		lock: false,

		/**
		 * When true, will close any other open exclusive tooltip before showing
		 * @var boolean
		 */
		exclusive: false,

		/**
		 * Animate show/hide
		 * @var boolean
		 */
		animate: false,

		/**
		 * Animate movement (positive value will move outwards)
		 * @var int
		 */
		animateMove: 10,

		/**
		 * Animate speed (time (ms) value or jQuery spped string)
		 * @var int|string
		 */
		animateSpeed: 'fast',

		/**
		 * Delay before showing the tooltip
		 * @var int
		 */
		delay: 0,

		/**
		 * Ajax content loading: url to load or Promise object returned by an $.ajax() call
		 * @var string|object
		 */
		ajax: null,

		/**
		 * Options for the ajax call (same as $.ajax())
		 * @var object
		 */
		ajaxOptions: {},

		/**
		 * Message to display in tooltip if ajax request fails (text or html)
		 * @var string
		 */
		ajaxErrorMessage: 'Error while loading data',

		/**
		 * Minimum distance from screen border
		 * @var int
		 */
		screenPadding: 10,

		/**
		 * Minimum spacing of tooltip arrow from border when tooltip is moved to fit in screen
		 * @var int
		 */
		arrowMargin: 10,

		/**
		 * Hide the tooltip when the mouse hovers out of the target element
		 * @var boolean
		 */
		removeOnMouseleave: false,

		/**
		 * Hide the tooltip when the user clicks anywhere else in the page
		 * @var boolean
		 */
		removeOnBlur: false,

		/**
		 * Hide the tooltip when the user clicks on the tooltip (only works if noPointerEvents is false)
		 * @var boolean
		 */
		removeOnClick: true,

		/**
		 * Callback on tooltip opening: function(target)
		 * Scope: the tooltip
		 * @var function
		 */
		onShow: null,

		/**
		 * Callback on tooltip remove: function(target)
		 * Note: the function may return false to prevent close.
		 * Scope: the tooltip
		 * @var function
		 */
		onRemove: null,

		/**
		 * Callback on delayed tooltip abort: function(target)
		 * Note: the function may return false to prevent abort.
		 * Scope: the target
		 * @var function
		 */
		onAbort: null
	};

	// Event binding
	if (!Modernizr || !Modernizr.touch)
	{
		doc.on('mouseenter', '.with-tooltip, .children-tooltip > *', function(event)
		{
			var element = $(this),
				parent = element.parent(),
				options = {
					delay:				100,
					removeOnMouseleave:	true
				};

			// Configuration for tooltips triggered by a parent element
			if (parent.hasClass('children-tooltip'))
			{
				options = $.extend(options, parent.data('tooltip-options'));
			}

			// Show tooltip
			element.tooltip(options);

		});
	}

})(jQuery, window, document);
/**
 *
 * '||''|.                            '||
 *  ||   ||    ....  .... ...   ....   ||    ...   ... ...  ... ..
 *  ||    || .|...||  '|.  |  .|...||  ||  .|  '|.  ||'  ||  ||' ''
 *  ||    || ||        '|.|   ||       ||  ||   ||  ||    |  ||
 * .||...|'   '|...'    '|     '|...' .||.  '|..|'  ||...'  .||.
 *                                                  ||
 * --------------- By Display:inline ------------- '''' -----------
 *
 * Content panel plugin
 *
 * Structural good practices from the article from Addy Osmani 'Essential jQuery plugin patterns'
 * @url http://coding.smashingmagazine.com/2011/10/11/essential-jquery-plugin-patterns/
 */

/*
 * The semi-colon before the function invocation is a safety
 * net against concatenated scripts and/or other plugins
 * that are not closed properly.
 */
;(function($, document)
{
	/*
	 * document is passed through as local variable rather than as a global, because this (slightly)
	 * quickens the resolution process and can be more efficiently minified.
	 */

	// Objects cache
	var doc = $(document);

	// Navigable menus
	doc.on('click', '.open-on-panel-content, .open-on-panel-navigation', function(event)
	{
		var link = $(event.target).closest('a'),
			contentPanel = link.closest('.content-panel'),
			href;

		// If not valid, exit
		if (!link.length || !contentPanel.length)
		{
			return;
		}

		// Target
		href = link.attr('href');
		if ($.trim(href).length === 0 || href == '#')
		{
			return;
		}

		// Stop normal behavior
		event.preventDefault();

		// Load content
		contentPanel[$(this).hasClass('open-on-panel-content') ? 'loadPanelContent' : 'loadPanelNavigation'](href);
	});

	/**
	 * Load navigation panel content with AJAX
	 * @param string url the url of the content to load
	 */
	$.fn.loadPanelNavigation = function(url)
	{
		return this.each(function(i)
		{
			var contentPanel = $(this).closest('.content-panel'),
				panelNavigation = contentPanel.children('.panel-navigation');

			// Load content
			loadPanelContent(url, contentPanel, panelNavigation, true);
		});
	};

	/**
	 * Load content panel content with AJAX
	 * @param string url the url of the content to load
	 */
	$.fn.loadPanelContent = function(url)
	{
		return this.each(function(i)
		{
			var contentPanel = $(this).closest('.content-panel'),
				panelContent = contentPanel.children('.panel-content');

			// Load content
			loadPanelContent(url, contentPanel, panelContent, false);
		});
	};

	/**
	 * Load content into a panel
	 * @param string url the url of the content to load
	 * @param jQuery wrapper the main block
	 * @param jQuery panel the panel in which to load content
	 * @param boolean isNav indicate if the panel is the navigation panel
	 */
	function loadPanelContent(url, wrapper, panel, isNav)
	{
		// If not valid, exit
		if (!wrapper.length || !panel.length)
		{
			return;
		}

			// Gather options
		var settings = $.extend({}, wrapper.data('panel-options'), panel.data('panel-options')),

			// Actual target
			loadTarget = panel.children('.panel-load-target:first'),
			target = loadTarget.length ? loadTarget : panel;

		// Pre-callback
		if (settings.onStartLoad)
		{
			if (settings.onStartLoad.call(target[0], settings) === false)
			{
				return;
			}
		}

		// Display panel (for mobile devices)
		wrapper[isNav ? 'removeClass' : 'addClass']('show-panel-content');

		// Load content
		$.ajax(url, $.extend({}, settings.ajax, {

			success: function(data, textStatus, jqXHR)
			{
				// Insert content
				target.html(data);

				// Callback in settings
				if (settings.ajax && settings.ajax.success)
				{
					settings.ajax.success.call(this, data, textStatus, jqXHR);
				}
			}

		}));
	};

	/**
	 * Enable content panel JS features
	 */
	$.fn.contentPanel = function()
	{
		return this.each(function(i)
		{
			var contentPanel = $(this).closest('.content-panel'),
				panelContent = contentPanel.children('.panel-content'),
				loadTarget, back, setMode;

			// If valid
			if (contentPanel.length > 0 && panelContent.length > 0)
			{
				// Enabled sliding panels on mobile
				contentPanel.addClass('enabled-panels');

				// Actual content block
				loadTarget = panelContent.children('.panel-load-target:first');

				// Create back button
				back = $('<div class="back"><span class="back-arrow"></span>Back</div>');
				if (loadTarget.length)
				{
					back.insertBefore(loadTarget);
				}
				else
				{
					back.prependTo(panelContent);
				}

				// Behavior
				back.click(function(event)
				{
					contentPanel.removeClass('show-panel-content');
				});

				// If not forced into permanent mobile mode
				if (!contentPanel.hasClass('mobile-panels'))
				{
					// Function to toggle mobile/desktop views
					setMode = function()
					{
						contentPanel[contentPanel.innerWidth() < 500 ? 'addClass' : 'removeClass']('mobile-panels');
					};

					// First run
					setMode();

					// Watch for size changes
					contentPanel.widthchange(setMode);
				}
			}
		});
	};

	// Add to template setup function
	$.template.addSetupFunction(function(self, children)
	{
		this.findIn(self, children, '.content-panel').contentPanel();

		return this;
	});

})(jQuery, document);
/**
 *
 * '||''|.                            '||
 *  ||   ||    ....  .... ...   ....   ||    ...   ... ...  ... ..
 *  ||    || .|...||  '|.  |  .|...||  ||  .|  '|.  ||'  ||  ||' ''
 *  ||    || ||        '|.|   ||       ||  ||   ||  ||    |  ||
 * .||...|'   '|...'    '|     '|...' .||.  '|..|'  ||...'  .||.
 *                                                  ||
 * --------------- By Display:inline ------------- '''' -----------
 *
 * Tabs plugin
 *
 * Structural good practices from the article from Addy Osmani 'Essential jQuery plugin patterns'
 * @url http://coding.smashingmagazine.com/2011/10/11/essential-jquery-plugin-patterns/
 */

/*
 * The semi-colon before the function invocation is a safety
 * net against concatenated scripts and/or other plugins
 * that are not closed properly.
 */
;(function($, window, document)
{
	/*
	 * window and document are passed through as local variables rather than as globals, because this (slightly)
	 * quickens the resolution process and can be more efficiently minified.
	 */

		// Objects cache
	var win = $(window),
		doc = $(document),

		// Indicate that a global refresh is on
		globalRefresh = false;

	// Event binding
	doc.on('click', '.tabs > li > a', function(event)
	{
		var link = $(this),
			tab = link.parent(),
			current;

		// Check if real tab link
		if (!this.href || this.href.indexOf('#') < 0)
		{
			return;
		}

		event.preventDefault();

		// If disabled, stop here
		if (tab.hasClass('disabled'))
		{
			return;
		}

		// Set as active and add animation class (the later the best for performance on document init)
		tab.addClass('active').closest('.standard-tabs, .swipe-tabs, .side-tabs').addClass('tabs-animated tab-opened');

		// Desactivate current active tab
		current = tab.siblings('.active').removeClass('active');

		// Refresh tabs
		link.refreshTabs();
	});

	/**
	 * Refresh tabs contained in an element
	 */
	$.fn.refreshInnerTabs = function()
	{
		// Tabs (processed in reverse order to start from deepest elements
		$(this.find('.standard-tabs, .swipe-tabs, .side-tabs').get().reverse()).refreshTabs();

		return this;
	};

	/**
	 * Refresh tabs: equalize height, show current tab...
	 */
	$.fn.refreshTabs = function()
	{
		var wrappers = $(),
			parents;

		this.each(function(i)
		{
				// Tabs wrapper
			var wrapper = $(this).closest('.standard-tabs, .swipe-tabs, .side-tabs'),

				// Tabs group
				tabs = wrapper.children('.tabs'),

				// Links of tabs
				tabsLinks = tabs.children().children('a[href^="#"]'),
				tabsLi = tabsLinks.parent(),

				// Tabs content
				tabsContent = wrapper.children('.tabs-content'),

				// Back button
				back = tabsContent.children('.tabs-back'),

				// Hidden parents
				hidden,

				// Processing vars
				equalized, active, activeId = false, activeHref,
				blocks = $(), newActive = false, datas = [],
				maxHeight = 0, tabsHeight;

			// IE7 has an issue with href attribute
			if ($.template.ie7)
			{
				tabsLinks = $();
				tabs.children().children('a').each(function(i)
				{
					if (this.href.indexOf('#') > -1)
					{
						tabsLinks = tabsLinks.add(this);
					}
				});
				tabsLi = tabsLinks.parent();
			}

			// If not found or not valid
			if (wrapper.length === 0 || tabs.length === 0 || tabsLinks.length === 0 || tabsContent.length === 0)
			{
				return;
			}

			// Add to wrappers list
			wrappers = wrappers.add(wrapper);

			// Create back button if needed
			if (back.length === 0)
			{
				back = $('<span class="tabs-back with-left-arrow top-bevel-on-light dark-text-bevel">Back</span>').prependTo(tabsContent).click(function(event)
				{
					// If the currently open tab contains a inner-tabs group
					var innerTabs = tabsContent.children('.tab-active:first').children('.inner-tabs.tab-opened'),
						backButton;
					if (innerTabs.length > 0)
					{
						// Click the back button
						backButton = innerTabs.children('.tabs-content').children('.tabs-back');
						if (backButton.length > 0)
						{
							backButton.click();
							return;
						}
					}

					// Return to tabs
					wrapper.removeClass('tab-opened');

					// Set wrapper correct size - will be ignored on standard/side tabs
					wrapper.height(tabs.outerHeight());

				}).applySetup(true, true);
			}

			// Reveal hidden parents if needed for correct height processing
			hidden = wrapper.tempShow();

			// Mode
			equalized = wrapper.hasClass('same-height');

			// Save height to prevent document scrolling
			if (equalized)
			{
				wrapper.css('min-height', wrapper.height()+'px');
			}

			// Active tab
			active = tabsLi.filter('.active:first');
			if (active.length === 0)
			{
				active = tabsLi.not('.disabled').first();
			}
			if (active.length > 0)
			{
				activeHref = active.addClass('active').children('a').attr('href');
				activeId = activeHref.substring(activeHref.indexOf('#')+1);
			}

			// Gather tabs content blocks and infos
			tabsLinks.each(function(i)
			{
				var linkHref = this.href,
					block = $(linkHref.substring(linkHref.indexOf('#'))),
					topMargin, height;

				// If found
				if (block.length > 0)
				{
					blocks = blocks.add(block.show());
					if (equalized)
					{
						// Get total height
						height = block.css('height', '').outerHeight();

						// Get first element's top-margin, because it affects the block height if it is negative
						topMargin = Math.min(0, block.children(':first').parseCSSValue('margin-top'));

						// Check if this is the tallest element
						maxHeight = Math.max(maxHeight, height+topMargin);

						// Store for equalization loop below
						datas[i] = [height, topMargin];
					}
				}
			});

			// Set equalized height
			if (equalized)
			{
				blocks.each(function(i)
				{
					var block = $(this),
						height = datas[i][0],
						topMargin = datas[i][1];

					// Set height depending on margins
					block.height(maxHeight-(height-block.height())-topMargin);
				});
			}

			// Toggle classes and hide non-active tabs
			blocks.each(function(i)
			{
				var block = $(this),
					isActive = block.hasClass('tab-active');

				// Hide if not current one
				if (this.id != activeId)
				{
					if (isActive)
					{
						block.removeClass('tab-active').trigger('hidetab');
					}
					block.hide();
				}
				else if (!isActive)
				{
					// Postponed call
					newActive = block.addClass('tab-active');
				}
			});

			// Send show event after hide events
			if (newActive)
			{
				// First time
				if (!newActive.data('tabshown'))
				{
					newActive.trigger('showtabinit');
					newActive.data('tabshown', true);
				}

				// Standard event
				newActive.trigger('showtab');
			}

			// Tabs height
			tabsHeight = tabs.height();

			// Content minimum size - ignored on standard tabs
			tabsContent.css('min-height', tabsHeight-1);	// 1 is the top border's size

			// For side-tabs, check if the content is smaller than the tabs
			if (wrapper.hasClass('side-tabs'))
			{
				wrapper[tabsContent.height() === (tabsHeight-1) ? 'addClass' : 'removeClass']('tabs-fullheight');
			}

			// Set wrapper correct size - ignored on standard/side tabs
			wrapper.height(wrapper.hasClass('tab-opened') ? tabsContent.outerHeight() : tabsHeight);

			// Restore height
			if (equalized)
			{
				wrapper.css('min-height', '');
			}

			// Hide previously hidden parents
			hidden.tempShowRevert();
		});

		// If not in a global refresh, update parent tabs
		if (!globalRefresh)
		{
			parents = wrappers.parent().closest('.standard-tabs, .swipe-tabs, .side-tabs').filter('.same-height').not(wrappers);
			if (parents.length > 0)
			{
				parents.refreshTabs();
			}
		}

		return this;
	};

	/**
	 * Add a tab
	 * @param string id the tab id
	 * @param string title the title of the tab
	 * @param string content the content of the tab
	 * @param boolean noPadding use true to prevent adding padding on the tab content block (optional, default: false)
	 * 
	 * Modified signature for PRISM
	 */
	$.fn.addTab = function(id, title, content, noPadding, assessmentId, reportId)
	{
		this.each(function(i)
		{
				// Tabs wrapper
			var wrapper = $(this).closest('.standard-tabs, .swipe-tabs, .side-tabs'),

				// Tabs group
				tabs = wrapper.children('.tabs'),

				// Tabs content
				tabsContent = wrapper.children('.tabs-content');

			// If not found or not valid
			if (wrapper.length === 0 || tabs.length === 0 || tabsContent.length === 0)
			{
				return;
			}

			// Create elements (and a little IE7 debug)
			//$('<li><a href="#'+id+'">'+title+'</a></li>').appendTo(tabs).prev().removeClass('last-child');
			//Changed for PRISM - added param in <li> - adding close span
			$('<li param="'+id+'" id="'+id+'-'+id+'"><a assessment="'+assessmentId+'" href="#'+id+'" id="'+id+'_'+id+'">'+title+'<span reportId="'+reportId+'" param="'+id+'" class="close closereport show-on-parent-hover">x</span></a></li>').appendTo(tabs).prev().removeClass('last-child');
			tabsContent.append('<div id="'+id+'"'+(noPadding ? '' : ' class="with-padding"')+'>'+content+'</div>');

			// Refresh tabs
			wrapper.refreshTabs();
		});

		return this;
	};

	/**
	 * Remove a tab: use it either on the tab or the content block. The tab should be valid for the method to work
	 */
	$.fn.removeTab = function()
	{
		this.each(function(i)
		{
				// Target element
			var target = $(this),

				// Closest parent
				parent = target.closest('.tabs, .tabs-content'),

				// Type
				isTab = parent.hasClass('tabs'),

				// Wrapper
				wrapper = parent.closest('.standard-tabs, .swipe-tabs, .side-tabs'),

				// Processing vars
				linkHref;

			// If not found or not valid
			if (parent.length === 0 || wrapper.length === 0)
			{
				return;
			}

			// If the target is a tab
			if (isTab)
			{
				// Find tab link
				target = target.is('a') ? target : target.children('a');
				linkHref = target.attr('href');

				// If not valid
				if (target.length === 0 || linkHref.indexOf('#') < 0)
				{
					return;
				}

				// Remove
				$(linkHref.substring(linkHref.indexOf('#'))).remove();
				if ($.template.ie7)
				{
					target.parent().prev().addClass('last-child');
				}
				target.parent().remove();
			}
			else
			{
				// Get content block
				while (target.length > 0 && !target.parent().hasClass('tabs-content'))
				{
					target = target.parent();
				}

				// Remove
				if ($.template.ie7)
				{
					wrapper.children('.tabs').children().children('a').each(function(i)
					{
						if (this.href.indexOf('#') > -1 && this.href.split('#')[1] == target.attr('id'))
						{
							$(this).parent().prev().addClass('last-child').next().remove();
						}
					});
				}
				else
				{
					wrapper.children('.tabs').find('a[href="#'+target.attr('id')+'"]').parent().remove();
				}
				target.remove();
			}

			// Refresh tabs
			wrapper.refreshTabs();
		});

		return this;
	};

	// Add template setup function
	$.template.addSetupFunction(function(self, children)
	{
		// Global mode
		globalRefresh = true;

		// Tabs (processed in reverse order to start from deepest elements
		$(this.findIn(self, children, '.standard-tabs, .swipe-tabs, .side-tabs').get().reverse()).addClass('tabs-active').refreshTabs();

		// End of global mode
		globalRefresh = false;

		return this;
	});

	// Handle screen resizing
	win.on('normalized-resize', function(event)
	{
		// Tabs (processed in reverse order to start from deepest elements
		$($('.standard-tabs, .swipe-tabs, .side-tabs').get().reverse()).refreshTabs();
	});

})(jQuery, window, document);
/**
 *
 * '||''|.                            '||
 *  ||   ||    ....  .... ...   ....   ||    ...   ... ...  ... ..
 *  ||    || .|...||  '|.  |  .|...||  ||  .|  '|.  ||'  ||  ||' ''
 *  ||    || ||        '|.|   ||       ||  ||   ||  ||    |  ||
 * .||...|'   '|...'    '|     '|...' .||.  '|..|'  ||...'  .||.
 *                                                  ||
 * --------------- By Display:inline ------------- '''' -----------
 *
 * Responsive tables plugin
 *
 * Structural good practices from the article from Addy Osmani 'Essential jQuery plugin patterns'
 * @url http://coding.smashingmagazine.com/2011/10/11/essential-jquery-plugin-patterns/
 */

/*
 * The semi-colon before the function invocation is a safety
 * net against concatenated scripts and/or other plugins
 * that are not closed properly.
 */
;(function($, window, document)
{
	/*
	 * document is passed through as local variable rather than as global, because this (slightly)
	 * quickens the resolution process and can be more efficiently minified.
	 */

		// Objects cache
	var win = $(window),
		doc = $(document),

		// Responsive classes
		responsiveClasses = [
			'hide-on-mobile-portrait',
			'hide-on-mobile-landscape',
			'hide-on-mobile',
			'hide-on-tablet-portrait',
			'hide-on-tablet-landscape',
			'hide-on-tablet',
			'forced-display'
		];

	/**
	 * Enable responsive tables: add classes to cells
	 */
	$.fn.responsiveTable = function()
	{
		if(!($.template.ie7 || $.template.ie8)) {
		// Init generic vars
		var classesList = responsiveClasses.join(' '),
			classesSelectors = '.'+responsiveClasses.join(', .');

		this.each(function(i)
		{
			var table = $(this).closest('table'),
				thead = table.children('thead'),
				cells = table.children('tbody').children().children();

			// Check if valid
			if (table.length === 0 || thead.length === 0 || cells.length === 0)
			{
				return;
			}

			// Global class
			table.addClass('responsive-table-on');

			// Clear cells classes
			cells.removeClass(classesSelectors);

			// Copy headers responsive classes
			thead.children().children().each(function(i)
			{
				var header = $(this),
					classes = [],
					c;

				// Find classes
				for (c = 0; c < responsiveClasses.length; ++c)
				{
					if (header.hasClass(responsiveClasses[c]))
					{
						classes.push(responsiveClasses[c]);
					}
				}

				// If any classes found
				if (classes.length > 0)
				{
					cells.filter(':nth-child('+(i+1)+')').addClass(classes.join(' '));
				}
			});
		});

		return this;
	}
	};

	// Add template setup function
	$.template.addSetupFunction(function(self, children)
	{
		this.findIn(self, children, '.responsive-table').responsiveTable();
	});

})(jQuery, window, document);
